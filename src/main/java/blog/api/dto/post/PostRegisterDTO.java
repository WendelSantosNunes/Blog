package blog.api.dto.post;

import blog.api.domain.user.User;
import jakarta.validation.constraints.NotBlank;

public record PostRegisterDTO(
        @NotBlank(message = "O campo título não pode ser nulo.")
        String title,

        @NotBlank(message = "O campo contéudo não pode ser nulo.")
        String content
) {
}
