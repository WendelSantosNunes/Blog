package blog.api.controllers;

import blog.api.domain.user.User;
import blog.api.dto.user.*;
import blog.api.services.TokenService;
import blog.api.services.UserPasswordResetService;
import blog.api.services.UserService;
import blog.api.util.GenericResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/auth")
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserPasswordResetService userPasswordResetService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserAuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body(new UserLoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO data){
        if(this.service.login(data.email()) != null){
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(
                data.name(),
                data.email(),
                data.phone(),
                encryptedPassword,
                data.role()
        );

        this.service.saveUser(newUser);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-user")
    public ResponseEntity update(@RequestBody @Valid UserUpdateDTO data,  @RequestHeader("Authorization") String token){
        token =  token.replace("Bearer ", "");

        DecodedJWT jwt = JWT.decode(token);
        String currentEmail = jwt.getSubject();

        if(!currentEmail.equals(data.email())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        this.service.userUpdate(
                new User(data.name(), data.email(), data.phone(), data.password(), data.role()),
                currentEmail
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity delete(@RequestBody @Valid UserDeleteDTO data, @RequestHeader("Authorization") String token){
        token =  token.replace("Bearer ", "");

        DecodedJWT jwt = JWT.decode(token);
        String currentEmail = jwt.getSubject();
        String currentUserRole = jwt.getClaim("role").asString();

        if(!currentUserRole.equals("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = this.service.getUser(data.id());

        if (currentEmail.equals(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setIsEnabled(false);

        this.service.UserDelete(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseGetDTO> getUser(@PathVariable Long id) {

        User user = this.service.getUser(id);
        UserResponseGetDTO userResponseGetDTO = new UserResponseGetDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getPosts()
        );

        return ResponseEntity.ok().body(userResponseGetDTO);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseGetDTO>> getAllUsers() {
        List<User> users = this.service.getAllUsers();
        List<UserResponseGetDTO> dtoList = users.stream()
                .map(user -> new UserResponseGetDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getCreatedAt(),
                        user.getPosts()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }

    // Recuperação de senha
    @PostMapping("/user/reset-password")
    public void resetPassword(HttpServletRequest request, @RequestBody @Valid UserResetDTO data){
        User user = this.service.getEmailUser(data.email());

        String token = UUID.randomUUID().toString();
        this.userPasswordResetService.createPasswordResetTokenForUser(token, user);

        mailSender.send(constructResetTokenEmail(getAppUrl(request),
                request.getLocale(), token, user));
    }

    @GetMapping("/user/change-password")
    public String showChangePasswordPage(Locale locale, final ModelMap model, @RequestParam("token") String token) {
        String result = userPasswordResetService.validatePasswordResetToken(token);
        if(result != null) {
            String message = messages.getMessage("auth.message." + result, null, locale);
            return "redirect:/login.html?lang="
                    + locale.getLanguage() + "&message=" + message;
        } else {
            model.addAttribute("token", token);
            return "redirect:/updatePassword.html?lang=" + locale.getLanguage() + "&token=" + token;
        }
    }

    // Save password
    @PostMapping("/user/save-password")
    public ResponseEntity<GenericResponse> savePassword(final Locale locale, @RequestBody @Valid UserPasswordDTO passwordDto) {
        final String result = userPasswordResetService.validatePasswordResetToken(passwordDto.token());

        if(result != null) {
            return ResponseEntity.badRequest().body(new GenericResponse(messages
                    .getMessage("auth.message." + result, null, locale)));
        }

        Optional<User> user = userPasswordResetService.getUserByPasswordResetToken(passwordDto.token());

        if(user.isPresent() && user.get().isEnabled()) {
            service.changeUserPassword(user.get(), passwordDto.newPassword());
            return ResponseEntity.ok().body(new GenericResponse(messages
                    .getMessage("message.resetPasswordSuc", null, locale)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(messages
                    .getMessage("auth.message.invalid", null, locale)));
        }
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath,
            Locale locale,
            String token,
            User user)
    {
        String url = contextPath + "/auth/user/change-password?token=" + token;
        String message = messages.getMessage("message.resetPassword",
                null, locale);

        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo("wendelnunes9999@gmail.com");
//        email.setTo(user.getEmail()); // Define o destinatário do email usando o email do usuário
//        email.setFrom(env.getProperty("support.email"));  // Define o remetente do email
        email.setFrom("testeseila20132@gmail.com");
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
