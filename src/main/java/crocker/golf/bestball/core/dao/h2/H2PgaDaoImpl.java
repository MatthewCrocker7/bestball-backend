package crocker.golf.bestball.core.dao.h2;

import crocker.golf.bestball.core.dao.ParamHelper;
import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.core.mapper.pga.*;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.*;
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
    private final String TOURNAMENT_COURSES = "TOURNAMENT_COURSES";
    private final String TOURNAMENT_ROUNDS = "TOURNAMENT_ROUNDS";
    private final String PLAYER_ROUNDS = "PLAYER_ROUNDS";

    private final String UPDATE_RANKINGS = "MERGE INTO " + WORLD_RANKINGS +
            " KEY(PLAYER_ID) VALUES(:playerId, :playerRank, :playerName);";

    private final String GET_WORLD_RANKINGS = "SELECT * FROM " + WORLD_RANKINGS;

    private final String UPDATE_SCHEDULE = "MERGE INTO " + SEASON_SCHEDULE +
            " KEY(TOURNAMENT_ID)" +
            " VALUES(:tournamentId, :eventType, :season, :tournamentState, :tournamentName, :startDate, :endDate);";

    private final String GET_SCHEDULE_BY_SEASON = "SELECT * FROM " + SEASON_SCHEDULE +
            " WHERE PGA_SEASON=:season;";

    private final String GET_TOURNAMENT_BY_ID = "SELECT * FROM " + SEASON_SCHEDULE +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_ALL_TOURNAMENTS = "SELECT * FROM " + SEASON_SCHEDULE + ";";

    private final String UPDATE_TOURNAMENT_COURSES = "MERGE INTO " + TOURNAMENT_COURSES +
            " KEY(TOURNAMENT_ID, COURSE_ID)" +
            " VALUES(:tournamentId, :courseId, :courseName, :yardage, :par, :holes);";

    private final String UPDATE_TOURNAMENT_ROUNDS = "MERGE INTO " + TOURNAMENT_ROUNDS +
            " KEY(TOURNAMENT_ID, ROUND_ID)" +
            " VALUES(:tournamentId, :roundId, :roundNumber, :status);";

    private final String GET_TOURNAMENT_COURSES_BY_ID = "SELECT * FROM " + TOURNAMENT_COURSES +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_TOURNAMENT_ROUNDS_BY_ID = "SELECT * FROM " + TOURNAMENT_ROUNDS +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String UPDATE_PLAYER_ROUNDS = "MERGE INTO " + PLAYER_ROUNDS +
            " KEY(PLAYER_ID, TOURNAMENT_ID, ROUND_ID)" +
            " VALUES(:playerId, :tournamentId, :roundId, :roundNumber," +
            " :courseId, :toPar, :thru, :strokes, :scores);";

    private final String GET_PLAYER_ROUNDS_BY_TOURNAMENT_ID = "SELECT * FROM " + PLAYER_ROUNDS +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    public H2PgaDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        MapSqlParameterSource[] params = ParamHelper.getPlayerParams(pgaPlayers);

        jdbcTemplate.batchUpdate(UPDATE_RANKINGS, params);
        logger.info("World golf rankings updated");
    }

    public List<PgaPlayer> getWorldRankings() {
        return jdbcTemplate.query(GET_WORLD_RANKINGS, new PgaPlayerMapper());
    }

    public void updateSeasonSchedule(List<Tournament> tournaments) {
        MapSqlParameterSource[] params = ParamHelper.getTournamentParams(tournaments);

        jdbcTemplate.batchUpdate(UPDATE_SCHEDULE, params);
        logger.info("Season schedule updated");
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

    public List<Tournament> getAllTournaments() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbcTemplate.query(GET_ALL_TOURNAMENTS, params, new TournamentRowMapper());
    }

    public void updateTournamentSummary(TournamentSummary tournamentSummary) {
        MapSqlParameterSource[] courseParams = ParamHelper.getTournamentCourseParams(tournamentSummary);
        MapSqlParameterSource[] roundParams = ParamHelper.getTournamentRoundParams(tournamentSummary);

        jdbcTemplate.batchUpdate(UPDATE_TOURNAMENT_COURSES, courseParams);
        jdbcTemplate.batchUpdate(UPDATE_TOURNAMENT_ROUNDS, roundParams);
    }

    public List<TournamentCourse> getTournamentCourses(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);
        return jdbcTemplate.query(GET_TOURNAMENT_COURSES_BY_ID, params, new TournamentCourseMapper());
    }

    public List<TournamentRound> getTournamentRounds(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);
        return jdbcTemplate.query(GET_TOURNAMENT_ROUNDS_BY_ID, params, new TournamentRoundMapper());
    }

    public void updatePlayerRounds(List<PlayerRound> playerRounds) {
        MapSqlParameterSource[] playerRoundParams = ParamHelper.getPlayerRoundParams(playerRounds);
        jdbcTemplate.batchUpdate(UPDATE_PLAYER_ROUNDS, playerRoundParams);
    }

    public List<PlayerRound> getPlayerRoundsByTournamentId(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);
        return jdbcTemplate.query(GET_PLAYER_ROUNDS_BY_TOURNAMENT_ID, params, new PlayerRoundMapper());
    }
}
