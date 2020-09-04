package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.core.mapper.UserInfoRowMapper;
import crocker.golf.bestball.domain.user.UserCredentials;

import crocker.golf.bestball.core.mapper.UserRowMapper;
import crocker.golf.bestball.domain.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.UUID;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String USER_SCHEMA = "USER_CREDENTIALS";

    private final String SAVE_USER = "INSERT INTO " + USER_SCHEMA +
            " (USER_ID, USER_NAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, ENABLED)" +
            " VALUES(:userId, :userName, :email, :password, :firstName, :lastName, :enabled);";

    private final String GET_USER_BY_USER_NAME = "SELECT * FROM " + USER_SCHEMA +
            " WHERE USER_NAME = :userName;";

    private final String GET_USER_BY_EMAIL = "SELECT * FROM " + USER_SCHEMA +
            " WHERE EMAIL = :email;";

    private final String GET_USER_BY_USER_ID = "SELECT * FROM " + USER_SCHEMA +
            " WHERE USER_ID = :userId;";

    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void save(UserCredentials userCredentials) {
        logger.info("Saving new user {}", userCredentials.getUserName());

        MapSqlParameterSource params = getUserParams(userCredentials);

        jdbcTemplate.update(SAVE_USER, params);
    }

    public UserCredentials findByUserName(String userName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userName", userName);
        return getUser(GET_USER_BY_USER_NAME, params);
    }

    public UserCredentials findByEmail(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        return getUser(GET_USER_BY_EMAIL, params);
    }

    public UserCredentials getUserByUserId(UUID userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        return getUser(GET_USER_BY_USER_ID, params);
    }

    private UserCredentials getUser(String query, MapSqlParameterSource params) {
        try {
            return jdbcTemplate.queryForObject(query, params, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found");
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private MapSqlParameterSource getUserParams(UserCredentials userCredentials) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userCredentials.getUserId());
        params.addValue("userName", userCredentials.getUserName());
        params.addValue("email", userCredentials.getEmail());
        params.addValue("password", userCredentials.getPassword());
        params.addValue("firstName", userCredentials.getFirstName());
        params.addValue("lastName", userCredentials.getLastName());
        params.addValue("enabled", userCredentials.isEnabled());

        return params;
    }
}
