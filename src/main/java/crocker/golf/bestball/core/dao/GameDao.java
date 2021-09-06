package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.game.Game;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.UUID;

public interface GameDao {

    void saveNewGame(Game game);

    void deleteGame(UUID gameId);

    Game getLatestGameByGameId(UUID gameId);

    Game getLatestGameByDraftId(UUID draftId);

    List<Game> getInProgressGames();
}
