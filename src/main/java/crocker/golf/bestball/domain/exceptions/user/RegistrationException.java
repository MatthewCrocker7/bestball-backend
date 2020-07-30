package crocker.golf.bestball.domain.exceptions.user;

import java.util.Map;

public class RegistrationException extends Exception {

    private Map<String, Exception> exceptions;

    public RegistrationException(String message, Map<String, Exception> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    public Map<String, Exception> getExceptions() {
        return exceptions;
    }

}
