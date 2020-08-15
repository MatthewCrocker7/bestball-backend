package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.game.participant.UserParticipant;
import crocker.golf.bestball.domain.user.UserCredentials;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserMapper {

    public UserDetails convertUserToUserDetails(UserCredentials userCredentials) {
        return User.withUsername(userCredentials.getUserName())
                .password(userCredentials.getPassword())
                .roles(new String[0])
                .build();
    }

    public UserParticipant convertUserCredentialsToParticipant(UserCredentials userCredentials) {
        return UserParticipant.builder()
                .userId(userCredentials.getUserId())

                .build();
    }
}
