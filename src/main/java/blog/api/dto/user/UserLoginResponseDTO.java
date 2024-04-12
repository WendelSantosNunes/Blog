package blog.api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginResponseDTO(
        @NotBlank(message = "O campo token não pode ser nulo.")
        String token
) {
}
