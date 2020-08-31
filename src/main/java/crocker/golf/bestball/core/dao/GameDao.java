package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.core.mapper.GameRowMapper;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.GameDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class GameDao {

    private static final Logger logger = LoggerFactory.getLogger(GameDao.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String GAMES = "GAMES";
    private final String SEASON_SCHEDULE = "SEASON_SCHEDULE";

    private final String SAVE_NEW_GAME = "INSERT INTO " + GAMES +
            " (GAME_ID, GAME_STATE, GAME_VERSION, GAME_TYPE, DRAFT_ID," +
            " TOURNAMENT_ID, NUM_PLAYERS, BUY_IN, MONEY_POT)" +
            " VALUES(:gameId, :gameState, :gameVersion, :gameType, :draftId, :tournamentId, :numPlayers, :buyIn, :moneyPot);";

    private final String GET_LATEST_GAME_BY_ID = "SELECT * FROM " + GAMES +
            " INNER JOIN " + SEASON_SCHEDULE +
            " ON " + GAMES + ".TOURNAMENT_ID = " + SEASON_SCHEDULE + ".TOURNAMENT_ID" +
            " WHERE (GAME_ID, GAME_VERSION) IN" +
            " (SELECT GAME_ID, MAX(GAME_VERSION) FROM " + GAMES +
            " WHERE GAME_ID=:gameId);";

    public GameDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNewGame(Game game) {
        MapSqlParameterSource params = getNewGameParams(game);
        jdbcTemplate.update(SAVE_NEW_GAME, params);
    }

    public Game getLatestGameById(UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);
        return jdbcTemplate.queryForObject(GET_LATEST_GAME_BY_ID, params, new GameRowMapper());
    }

    private MapSqlParameterSource getNewGameParams(Game game) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", game.getGameId());
        params.addValue("gameState", game.getGameState().name());
        params.addValue("gameVersion", game.getGameVersion());
        params.addValue("gameType", game.getGameType().name());
        params.addValue("draftId", game.getDraftId());
        params.addValue("tournamentId", game.getTournament().getTournamentId());
        params.addValue("numPlayers", game.getNumPlayers());
        params.addValue("buyIn", game.getBuyIn());
        params.addValue("moneyPot", game.getMoneyPot());

        return params;
    }
}
