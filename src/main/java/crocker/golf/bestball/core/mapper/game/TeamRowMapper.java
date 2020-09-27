package crocker.golf.bestball.core.mapper.game;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.domain.enums.game.TeamRole;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class TeamRowMapper implements RowMapper<Team> {

    private PgaRepository pgaRepository;

    public TeamRowMapper(PgaRepository pgaRepository) {
        this.pgaRepository = pgaRepository;
    }

    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Team.builder()
                .teamId((UUID)rs.getObject("TEAM_ID"))
                .userId((UUID)rs.getObject("USER_ID"))
                .draftId((UUID)rs.getObject("DRAFT_ID"))
                .gameId((UUID)rs.getObject("GAME_ID"))
                .teamRole(TeamRole.valueOf(rs.getString("TEAM_ROLE")))
                .golferOne(buildGolfer(rs, "PLAYER_ONE_ID"))
                .golferTwo(buildGolfer(rs, "PLAYER_TWO_ID"))
                .golferThree(buildGolfer(rs, "PLAYER_THREE_ID"))
                .golferFour(buildGolfer(rs, "PLAYER_FOUR_ID"))
                .build();
    }

    private PgaPlayer buildGolfer(ResultSet rs, String column) throws SQLException {
        Map<UUID, PgaPlayer> pgaPlayers = pgaRepository.getWorldRankingsAsMap();

        UUID golferId = (UUID)rs.getObject(column);

        if (golferId == null) {
            return null;
        }

        return pgaPlayers.get(golferId);
    }
}
