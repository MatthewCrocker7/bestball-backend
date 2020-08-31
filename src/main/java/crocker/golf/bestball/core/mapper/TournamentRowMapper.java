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
                .season(rs.getInt("PGA_SEASON"))
                .tournamentState(TournamentState.valueOf(rs.getString("TOURNAMENT_STATE")))
                .name(rs.getString("TOURNAMENT_NAME"))
                .startDate(rs.getTimestamp("TOURNAMENT_START_DATE").toLocalDateTime())
                .endDate(rs.getDate("TOURNAMENT_END_DATE").toLocalDate())
                .build();
    }
}
