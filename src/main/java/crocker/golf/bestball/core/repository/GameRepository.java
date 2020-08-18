package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.GameDao;
import crocker.golf.bestball.domain.game.Draft;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.GameDto;

public class GameRepository {

    private GameDao gameDao;

    public GameRepository(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void saveNewGame(Game game) {
        gameDao.saveNewGame(game);
    }

    public void saveNewDraft(Draft draft) {}

    public Game getGameById(GameDto gameDto) { return gameDao.getGameById(gameDto); }

}
