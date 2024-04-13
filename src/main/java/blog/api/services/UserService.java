package blog.api.services;

import blog.api.domain.user.User;
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

//        if(currentUser == null){
//            return ResponseEntity.badRequest().build();
//        }

        String encruptedPassword = new BCryptPasswordEncoder().encode(updateUser.getPassword());

        currentUser.setName(updateUser.getName());
        currentUser.setEmail(updateUser.getEmail());
        currentUser.setPassword(encruptedPassword);
        currentUser.setPhone(updateUser.getPhone());
        currentUser.setRole(updateUser.getRole());

        this.repository.save(currentUser);
    }

    public Optional<User> getUser(Long id) {
        return this.repository.findById(id);
    }

    public void UserDelete(User dados) {
        this.repository.save(dados);
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }
}
