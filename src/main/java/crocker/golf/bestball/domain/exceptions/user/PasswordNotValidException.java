package crocker.golf.bestball.domain.exceptions.user;

public class PasswordNotValidException extends Exception {
    public PasswordNotValidException(String message) {
        super(message);
    }
}
