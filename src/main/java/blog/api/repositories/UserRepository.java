package blog.api.repositories;

import blog.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findUserDetailsByEmail(String email);
    User findByEmail(String email);
    @Query(value = "SELECT * FROM users u JOIN posts p ON u.id = p.user_id WHERE u.id = :userId", nativeQuery = true)
    Optional<User> findByIdWithPosts(@Param("userId") Long userId);
}
