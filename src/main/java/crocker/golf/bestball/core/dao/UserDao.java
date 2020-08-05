package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.user.UserCredentials;

import crocker.golf.bestball.core.mapper.UserRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private JdbcTemplate jdbcTemplate;

    private final String SAVE_USER = "INSERT INTO USERS " +
            "(USER_ID, USER_NAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, ENABLED) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?);";

    private final String GET_USER_BY_USER_NAME = "SELECT * FROM USERS WHERE " +
            "USER_NAME = ?;";

    private final String GET_USER_BY_EMAIL = "SELECT * FROM USERS WHERE " +
            "EMAIL = ?;";

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void save(UserCredentials userCredentials) {
        logger.info("Saving new user {}", userCredentials.getUserName());

        Object[] params = new Object[]{
                userCredentials.getUserId(),
                userCredentials.getUserName(),
                userCredentials.getEmail(),
                userCredentials.getPassword(),
                userCredentials.getFirstName(),
                userCredentials.getLastName(),
                userCredentials.isEnabled()
        };

        jdbcTemplate.update(SAVE_USER, params);
    }

    public UserCredentials findByUserName(String username) {
        Object[] params = new Object[]{username};
        return getUser(GET_USER_BY_USER_NAME, params);
    }

    public UserCredentials findByEmail(String email) {
        Object[] params = new Object[]{email};
        return getUser(GET_USER_BY_EMAIL, params);
    }

    private UserCredentials getUser(String query, Object[] params) {
        try {
            return jdbcTemplate.queryForObject(query, params, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
