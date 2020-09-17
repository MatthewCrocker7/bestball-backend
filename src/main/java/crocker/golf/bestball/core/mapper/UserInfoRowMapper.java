package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserInfoRowMapper implements RowMapper<UserInfo> {

    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserInfo.builder()
                .userId((UUID)rs.getObject("USER_ID"))
                .userName(rs.getString("USER_NAME"))
                .pickNumber(rs.getInt("PICK_NUMBER"))
                .email(rs.getString("EMAIL"))
                .build();
    }
}
