package crocker.golf.bestball.domain.exceptions.user;

public class EmailNotUniqueException extends Exception {
    public EmailNotUniqueException(String message) {
        super(message);
    }
}
