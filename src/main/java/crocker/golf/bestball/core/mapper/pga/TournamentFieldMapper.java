package crocker.golf.bestball.core.mapper.pga;

import crocker.golf.bestball.domain.pga.PgaPlayer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TournamentFieldMapper implements RowMapper<PgaPlayer> {

    @Override
    public PgaPlayer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PgaPlayer.builder()
                .playerId((UUID)rs.getObject("PLAYER_ID"))
                .build();
    }
}
