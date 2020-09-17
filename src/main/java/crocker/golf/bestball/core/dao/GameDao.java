package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.game.Game;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.UUID;

public interface GameDao {

    void saveNewGame(Game game);

    Game getLatestGameByGameId(UUID gameId);

    Game getLatestGameByDraftId(UUID draftId);
}
