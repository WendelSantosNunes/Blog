package blog.api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationDTO(

       @NotBlank(message = "O campo email não pode ser nulo.")
       String email,

       @NotBlank(message = "O campo senha não pode ser nulo.")
       String password
) {
}
