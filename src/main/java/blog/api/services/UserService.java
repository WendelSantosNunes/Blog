package blog.api.services;

import blog.api.domain.user.User;
import blog.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
}
