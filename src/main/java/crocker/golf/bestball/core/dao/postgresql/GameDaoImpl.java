package crocker.golf.bestball.core.dao.postgresql;

import crocker.golf.bestball.core.dao.GameDao;
import crocker.golf.bestball.core.dao.ParamHelper;
import crocker.golf.bestball.core.mapper.game.GameRowMapper;
import crocker.golf.bestball.domain.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.tags.Param;

import java.util.List;
import java.util.UUID;

public class GameDaoImpl implements GameDao {

    private static final Logger logger = LoggerFactory.getLogger(GameDaoImpl.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String GAMES = "GAMES";
    private final String SEASON_SCHEDULE = "SEASON_SCHEDULE";

    private final String SAVE_NEW_GAME = "INSERT INTO " + GAMES +
            " (GAME_ID, GAME_STATE, GAME_VERSION, GAME_TYPE, DRAFT_ID," +
            " TOURNAMENT_ID, NUM_PLAYERS, BUY_IN, MONEY_POT)" +
            " VALUES(:gameId, :gameState, :gameVersion, :gameType, :draftId, :tournamentId, :numPlayers, :buyIn, :moneyPot);";

    private final String UPDATE_GAME = "UPDATE " + GAMES +
            " SET GAME_STATE=:gameState, GAME_VERSION=:gameVersion" +
            " WHERE GAME_ID=:gameId;";

    private final String DELETE_GAME = "DELETE FROM " + GAMES +
            " WHERE GAME_ID=:gameId;";

    private final String GET_LATEST_GAME_BY_GAME_ID = "SELECT * FROM " + GAMES +
            " INNER JOIN " + SEASON_SCHEDULE +
            " ON " + GAMES + ".TOURNAMENT_ID = " + SEASON_SCHEDULE + ".TOURNAMENT_ID" +
            " WHERE (GAME_ID, GAME_VERSION) IN" +
            " (SELECT GAME_ID, MAX(GAME_VERSION) FROM " + GAMES +
            " WHERE GAME_ID=:gameId GROUP BY GAME_ID);";

    private final String GET_LATEST_GAME_BY_DRAFT_ID = "SELECT * FROM " + GAMES +
            " INNER JOIN " + SEASON_SCHEDULE +
            " ON " + GAMES + ".TOURNAMENT_ID = " + SEASON_SCHEDULE + ".TOURNAMENT_ID" +
            " WHERE (GAME_ID, GAME_VERSION) IN" +
            " (SELECT GAME_ID, MAX(GAME_VERSION) FROM " + GAMES +
            " WHERE DRAFT_ID=:draftId GROUP BY GAME_ID);";

    private final String GET_IN_PROGRESS_GAMES = "SELECT * FROM " + GAMES +
            " INNER JOIN " + SEASON_SCHEDULE +
            " ON " + GAMES + ".TOURNAMENT_ID = " + SEASON_SCHEDULE + ".TOURNAMENT_ID" +
            " WHERE GAME_STATE='IN_PROGRESS';";

    public GameDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNewGame(Game game) {
        MapSqlParameterSource params = ParamHelper.getNewGameParams(game);
        jdbcTemplate.update(SAVE_NEW_GAME, params);
    }

    public void updateGames(List<Game> games) {
        MapSqlParameterSource[] params = ParamHelper.getBatchGameParams(games);

        jdbcTemplate.batchUpdate(UPDATE_GAME, params);
    }

    public void deleteGame(UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);

        jdbcTemplate.update(DELETE_GAME, params);
    }

    public Game getLatestGameByGameId(UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);
        return jdbcTemplate.queryForObject(GET_LATEST_GAME_BY_GAME_ID, params, new GameRowMapper());
    }

    public Game getLatestGameByDraftId(UUID draftId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);
        return jdbcTemplate.queryForObject(GET_LATEST_GAME_BY_DRAFT_ID, params, new GameRowMapper());
    }

    public List<Game> getInProgressGames() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbcTemplate.query(GET_IN_PROGRESS_GAMES, params, new GameRowMapper());
    }

}
