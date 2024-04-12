package blog.api.repositories;

import blog.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findUserDetailsByEmail(String email);
    User findByEmail(String email);
}
