package blog.api.domain.user.enumUser;

public enum UserRole {
    ADMIN("admin"),
    STANDARD("standard");

    private final String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
