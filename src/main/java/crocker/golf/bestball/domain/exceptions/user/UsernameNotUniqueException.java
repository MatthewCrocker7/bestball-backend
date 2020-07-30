package crocker.golf.bestball.domain.exceptions.user;

public class UsernameNotUniqueException extends Exception {
    public UsernameNotUniqueException(String message) {
        super(message);
    }
}
