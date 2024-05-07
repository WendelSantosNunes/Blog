package blog.api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRegisterDTO(
        @NotBlank(message = "O campo contéudo não pode ser nulo.")
        String content,

        @NotNull(message = "O campo contéudo não pode ser nulo.")
        Long post_id
) {

}
