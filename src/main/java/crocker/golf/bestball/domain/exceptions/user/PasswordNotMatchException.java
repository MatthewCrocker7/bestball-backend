package crocker.golf.bestball.domain.exceptions.user;

public class PasswordNotMatchException extends Exception {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
