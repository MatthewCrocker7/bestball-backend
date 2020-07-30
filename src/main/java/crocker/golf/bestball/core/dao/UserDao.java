package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.user.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void save(UserCredentials userCredentials) {
        logger.info("Saving new user {}", userCredentials.getUserName());
    }

    public UserCredentials findByUserName(String username) {
        return UserCredentials.builder()
                .build();
    }

    public UserCredentials findByEmail(String email) {
        return UserCredentials.builder()
                .build();
    }
}
