package crocker.golf.bestball.core.dao.h2;

import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.core.mapper.PgaPlayerMapper;
import crocker.golf.bestball.core.mapper.TournamentRowMapper;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class H2PgaDaoImpl implements PgaDao {

    private static final Logger logger = LoggerFactory.getLogger(H2PgaDaoImpl.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String WORLD_RANKINGS = "WORLD_RANKINGS";
    private final String SEASON_SCHEDULE = "SEASON_SCHEDULE";

    private final String DELETE_RANKINGS = "DELETE FROM " + WORLD_RANKINGS + ";";
    private final String DELETE_SCHEDULE = "DELETE FROM " + SEASON_SCHEDULE + ";";

    private final String UPDATE_RANKINGS = "INSERT INTO " + WORLD_RANKINGS +
            " (PLAYER_ID, PLAYER_RANK, PLAYER_NAME)" +
            " VALUES(:playerId, :playerRank, :playerName);";

    private final String UPDATE_SCHEDULE = "INSERT INTO " + SEASON_SCHEDULE +
            " (TOURNAMENT_ID, EVENT_TYPE, PGA_SEASON, TOURNAMENT_STATE, TOURNAMENT_NAME," +
            " TOURNAMENT_START_DATE, TOURNAMENT_END_DATE)" +
            " VALUES(:tournamentId, :eventType, :season, :tournamentState, :tournamentName, :startDate, :endDate);";

    private final String GET_WORLD_RANKINGS = "SELECT * FROM " + WORLD_RANKINGS;

    private final String GET_SCHEDULE_BY_SEASON = "SELECT * FROM " + SEASON_SCHEDULE +
            " WHERE PGA_SEASON=:season;";

    private final String GET_TOURNAMENT_BY_ID = "SELECT * FROM " + SEASON_SCHEDULE +
            " WHERE TOURNAMENT_ID=:tournamentId;";


    public H2PgaDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        MapSqlParameterSource[] params = getPlayerParams(pgaPlayers);

        //TODO: Need to add on conflict update
        jdbcTemplate.batchUpdate(UPDATE_RANKINGS, params);
        logger.info("World golf rankings updated");
    }

    public void updateSeasonSchedule(List<Tournament> tournaments) {
        MapSqlParameterSource[] params = getTournamentParams(tournaments);

        jdbcTemplate.batchUpdate(UPDATE_SCHEDULE, params);
        logger.info("Season schedule updated");
    }

    public List<PgaPlayer> getWorldRankings() {
        return jdbcTemplate.query(GET_WORLD_RANKINGS, new PgaPlayerMapper());
    }

    public List<Tournament> getTournamentsBySeason(int year) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("season", year);
        return jdbcTemplate.query(GET_SCHEDULE_BY_SEASON, params, new TournamentRowMapper());
    }

    public Tournament getTournamentById(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);
        return jdbcTemplate.queryForObject(GET_TOURNAMENT_BY_ID, params, new TournamentRowMapper());
    }

    private MapSqlParameterSource[] getPlayerParams(List<PgaPlayer> pgaPlayers) {
        return pgaPlayers.stream().map(pgaPlayer -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("playerId", pgaPlayer.getPlayerId());
            params.addValue("playerRank", pgaPlayer.getRank());
            params.addValue("playerName", pgaPlayer.getPlayerName());

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] getTournamentParams(List<Tournament> tournaments) {
        return tournaments.stream().map(tournament -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("tournamentId", tournament.getTournamentId());
            params.addValue("eventType", tournament.getEventType().name());
            params.addValue("season", tournament.getSeason());
            params.addValue("tournamentState", tournament.getTournamentState().name());
            params.addValue("tournamentName", tournament.getName());
            params.addValue("startDate", tournament.getStartDate());
            params.addValue("endDate", tournament.getEndDate());

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }

}
