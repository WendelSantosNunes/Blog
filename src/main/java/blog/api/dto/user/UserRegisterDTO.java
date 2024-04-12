package blog.api.dto.user;

import blog.api.domain.user.enumUser.UserRole;

public record UserRegisterDTO(String name, String email, String phone, String password, UserRole role) {
}