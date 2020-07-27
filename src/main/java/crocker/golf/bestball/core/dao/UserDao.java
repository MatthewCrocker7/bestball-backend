package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.UserCredentials;

public class UserDao {

    public void save(UserCredentials userCredentials) {

    }

    public UserCredentials findByUserName(String username) {
        return UserCredentials.builder()
                .build();
    }
}
