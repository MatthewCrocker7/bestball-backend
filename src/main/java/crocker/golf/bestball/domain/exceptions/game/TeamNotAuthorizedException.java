package crocker.golf.bestball.domain.exceptions.game;

public class TeamNotAuthorizedException extends Exception {
    public TeamNotAuthorizedException(String message) {
        super(message);
    }
}
