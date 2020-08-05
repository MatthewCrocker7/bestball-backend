package crocker.golf.bestball.domain.exceptions.user;

public class UserNotExistException extends Exception {
    public UserNotExistException(String message) {
        super(message);
    }
}
