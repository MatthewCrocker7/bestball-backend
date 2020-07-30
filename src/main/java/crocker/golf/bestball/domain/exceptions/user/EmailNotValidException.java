package crocker.golf.bestball.domain.exceptions.user;

public class EmailNotValidException extends Exception {
    public EmailNotValidException(String message) {
        super(message);
    }
}
