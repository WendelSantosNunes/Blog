package blog.api.dto.user;

import blog.api.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseGetDTO(
        Long id,
        String name,
        String email,
        String phone,
        LocalDateTime createdAt,
        List<Post> posts
) {
}
