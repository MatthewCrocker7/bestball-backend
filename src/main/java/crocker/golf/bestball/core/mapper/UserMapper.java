package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.UserCredentials;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserMapper {

    public UserDetails convertUserToUserDetails(UserCredentials userCredentials) {
        return User.withUsername(userCredentials.getUserName())
                .password(userCredentials.getPassword())
                .roles(userCredentials.getRoles().toArray(new String[0]))
                .build();
    }
}
