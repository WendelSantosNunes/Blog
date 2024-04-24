package blog.api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserResetDTO(
        @NotBlank(message = "O campo email não pode ser nulo.")
        String email
) {
}
