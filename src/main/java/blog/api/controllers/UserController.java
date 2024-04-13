package blog.api.controllers;

import blog.api.domain.user.User;
import blog.api.dto.user.*;
import blog.api.services.TokenService;
import blog.api.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/auth")
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

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

        Optional<User> optionalUser = this.service.getUser(data.id());

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();

        if (currentEmail.equals(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setIsEnabled(false);

        this.service.UserDelete(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return this.service.getUser(id);
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return this.service.getAllUsers();
    }
}
