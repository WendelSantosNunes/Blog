package blog.api.services;

import blog.api.domain.user.User;
import blog.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
