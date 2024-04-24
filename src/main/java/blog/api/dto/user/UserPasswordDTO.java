package blog.api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserPasswordDTO(
        @NotBlank(message = "O campo nome não pode ser token.")
        String token,

        @NotBlank(message = "O campo nome não pode ser newPassowrd.")
        String newPassword
) {
}
