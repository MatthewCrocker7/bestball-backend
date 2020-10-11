package crocker.golf.bestball.core.service.user;

import crocker.golf.bestball.domain.events.RegisterNewUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public class RegistrationEventListener implements ApplicationListener<RegisterNewUserEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationEventListener.class);

    @Override
    public void onApplicationEvent(RegisterNewUserEvent userEvent) {
        logger.info("Sending confirm email to user {} linking to {}", userEvent.getUserCredentials().getUserName(), userEvent.getUrl());
    }


}
