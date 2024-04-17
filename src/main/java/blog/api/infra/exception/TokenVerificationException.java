package blog.api.infra.exception;

public class TokenVerificationException extends RuntimeException{
    public TokenVerificationException(String message) {
        super(message);
    }
}
