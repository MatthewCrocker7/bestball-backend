package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.core.mapper.GameRowMapper;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.GameDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class GameDao {

    private static final Logger logger = LoggerFactory.getLogger(GameDao.class);

    private JdbcTemplate jdbcTemplate;

    private final String GAMES = "GAMES";

    private final String SAVE_NEW_GAME = "INSERT INTO " + GAMES +
            " (GAME_ID, GAME_STATE, GAME_VERSION, GAME_TYPE, DRAFT_ID," +
            " TOURNAMENT_ID, NUM_PLAYERS, MONEY_POT)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

    private final String GET_GAME_BY_ID = "SELECT * FROM " + GAMES +
            " WHERE GAME_ID=?;";

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNewGame(Game game) {
        Object[] params = getNewGameParams(game);
        jdbcTemplate.update(SAVE_NEW_GAME, params);
    }

    public Game getGameById(GameDto gameDto) {
        Object[] params = new Object[]{gameDto.getGameId()};
        return jdbcTemplate.queryForObject(GET_GAME_BY_ID, params, new GameRowMapper());
    }

    private Object[] getNewGameParams(Game game) {
        return new Object[] {
                game.getGameId(),
                game.getGameState().name(),
                game.getGameVersion(),
                game.getGameType().name(),
                game.getDraftId(),
                game.getTournament().getTournamentId(),
                game.getNumPlayers(),
                game.getMoneyPot()
        };
    }
}
