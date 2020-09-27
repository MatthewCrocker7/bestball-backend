package crocker.golf.bestball.core.mapper.pga;

import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PgaPlayerMapper  implements RowMapper<PgaPlayer> {

    @Override
    public PgaPlayer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PgaPlayer.builder()
                .playerId((UUID)rs.getObject("PLAYER_ID"))
                .rank(rs.getInt("PLAYER_RANK"))
                .playerName(rs.getString("PLAYER_NAME"))
                .build();
    }
}
