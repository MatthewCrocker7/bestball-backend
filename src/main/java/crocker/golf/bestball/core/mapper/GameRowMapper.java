package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.enums.game.GameState;
import crocker.golf.bestball.domain.enums.game.GameType;
import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.pga.Tournament;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GameRowMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Game.builder()
                .gameId((UUID)rs.getObject("GAME_ID"))
                .gameState(GameState.valueOf(rs.getString("GAME_STATE")))
                .gameVersion(rs.getInt("GAME_VERSION"))
                .gameType(GameType.valueOf(rs.getString("GAME_TYPE")))
                .draftId((UUID)rs.getObject("DRAFT_ID"))
                .numPlayers(rs.getInt("NUM_PLAYERS"))
                .moneyPot((BigDecimal)rs.getObject("MONEY_POT"))
                .build();
    }
}
