package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.enums.game.TeamRole;
import crocker.golf.bestball.domain.game.Team;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TeamRowMapper implements RowMapper<Team> {

    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Team.builder()
                .teamId((UUID)rs.getObject("TEAM_ID"))
                .userId((UUID)rs.getObject("USER_ID"))
                .draftId((UUID)rs.getObject("DRAFT_ID"))
                .gameId((UUID)rs.getObject("GAME_ID"))
                .teamRole(TeamRole.valueOf(rs.getString("TEAM_ROLE")))
                .build();
    }
}
