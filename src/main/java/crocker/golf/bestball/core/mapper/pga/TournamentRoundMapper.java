package crocker.golf.bestball.core.mapper.pga;

import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.Status;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentRound;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TournamentRoundMapper implements RowMapper<TournamentRound> {

    @Override
    public TournamentRound mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TournamentRound.builder()
                .tournamentId((UUID)rs.getObject("TOURNAMENT_ID"))
                .roundId((UUID)rs.getObject("ROUND_ID"))
                .roundNumber(rs.getInt("ROUND_NUMBER"))
                .roundStatus(Status.valueOf(rs.getString("STATUS")))
                .build();
    }
}
