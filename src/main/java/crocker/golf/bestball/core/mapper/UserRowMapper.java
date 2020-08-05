package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.user.UserCredentials;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRowMapper implements RowMapper<UserCredentials> {

    @Override
    public UserCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserCredentials.builder()
                .userId(rs.getObject("USER_ID", UUID.class))
                .userName(rs.getString("USER_NAME"))
                .email(rs.getString("EMAIL"))
                .password(rs.getString("PASSWORD"))
                .firstName(rs.getString("FIRST_NAME"))
                .lastName(rs.getString("LAST_NAME"))
                .enabled(rs.getBoolean("ENABLED"))
                .build();
    }
}
