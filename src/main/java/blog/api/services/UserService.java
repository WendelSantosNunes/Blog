package blog.api.services;

import blog.api.domain.passwordResetToken.PasswordResetToken;
import blog.api.domain.user.User;
import blog.api.domain.user.exceptions.UserNotFoundException;
import blog.api.repositories.PasswordResetTokenRepository;
import blog.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public UserDetails login(String email) {
        return this.repository.findUserDetailsByEmail(email);
    }

    public void saveUser(User newUser){
        this.repository.save(newUser);
    }

    public void userUpdate(User updateUser, String email) {
        User currentUser = this.repository.findByEmail(email);

        String encruptedPassword = new BCryptPasswordEncoder().encode(updateUser.getPassword());

        currentUser.setName(updateUser.getName());
        currentUser.setEmail(updateUser.getEmail());
        currentUser.setPassword(encruptedPassword);
        currentUser.setPhone(updateUser.getPhone());
        currentUser.setRole(updateUser.getRole());

        this.repository.save(currentUser);
    }

    public User getUser(Long id) {
        User newUser = this.repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id));

        if(!newUser.isEnabled()){
            throw new UserNotFoundException("User not found with id: " + id);
        }

        return newUser;
    }

    public User getEmailUser(String email) {
        User newUser = this.repository.findByEmail(email);

        if(newUser == null || !newUser.isEnabled()){
            throw new UserNotFoundException("User not found with email: " + email);
        }

        return newUser;
    }

    public void UserDelete(User dados) {
        this.repository.save(dados);
    }

    public List<User> getAllUsers() {
        return this.repository.findAll().stream().filter(user -> user.isEnabled()).toList();
    }

    public void changeUserPassword(User user, String newPassword) {
        String encruptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encruptedPassword);
        this.repository.save(user);
    }
}
