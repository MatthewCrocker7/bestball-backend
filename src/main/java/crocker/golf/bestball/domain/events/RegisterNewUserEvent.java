package crocker.golf.bestball.domain.events;

import crocker.golf.bestball.domain.user.UserCredentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class RegisterNewUserEvent extends ApplicationEvent {

    private UserCredentials userCredentials;
    private String url;

    public RegisterNewUserEvent(UserCredentials userCredentials, String url) {
        super(userCredentials);

        this.userCredentials = userCredentials;
        this.url = url;
    }
}
