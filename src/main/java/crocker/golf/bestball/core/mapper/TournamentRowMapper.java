package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.Tournament;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class TournamentRowMapper implements RowMapper<Tournament> {

    @Override
    public Tournament mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tournament.builder()
                .tournamentId((UUID)rs.getObject("TOURNAMENT_ID"))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .season(rs.getInt("SEASON"))
                .tournamentState(TournamentState.valueOf(rs.getString("STATE")))
                .name(rs.getString("NAME"))
                .startDate(rs.getTimestamp("START_DATE").toLocalDateTime())
                .endDate(rs.getDate("END_DATE").toLocalDate())
                .build();
    }
}
