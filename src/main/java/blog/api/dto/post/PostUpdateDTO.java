package blog.api.dto.post;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateDTO(
        @NotBlank(message = "O campo título não pode ser nulo.")
        String title,

        @NotBlank(message = "O campo contéudo não pode ser nulo.")
        String content
) {
}
