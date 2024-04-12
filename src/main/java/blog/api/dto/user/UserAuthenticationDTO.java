package blog.api.dto.user;

public record UserAuthenticationDTO(
       String email,
       String password
) {
}
