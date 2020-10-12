package crocker.golf.bestball.core.dao.postgresql;

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
import java.util.stream.Collectors;

public class PgaDaoImpl implements PgaDao {

    private static final Logger logger = LoggerFactory.getLogger(PgaDaoImpl.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String WORLD_RANKINGS = "WORLD_RANKINGS";
    private final String SEASON_SCHEDULE = "SEASON_SCHEDULE";
    private final String TOURNAMENT_FIELD = "TOURNAMENT_FIELD";
    private final String TOURNAMENT_COURSES = "TOURNAMENT_COURSES";
    private final String TOURNAMENT_ROUNDS = "TOURNAMENT_ROUNDS";
    private final String PLAYER_ROUNDS = "PLAYER_ROUNDS";

    private final String UPDATE_RANKINGS = "INSERT INTO " + WORLD_RANKINGS +
            " (PLAYER_ID, PLAYER_RANK, PLAYER_NAME)" +
            " VALUES(:playerId, :playerRank, :playerName)" +
            " ON CONFLICT (PLAYER_ID) DO UPDATE SET" +
            " PLAYER_RANK = :playerRank, PLAYER_NAME = :playerName;";

    private final String UPDATE_SCHEDULE = "INSERT INTO " + SEASON_SCHEDULE +
            " (TOURNAMENT_ID, EVENT_TYPE, PGA_SEASON, TOURNAMENT_STATE, TOURNAMENT_NAME," +
            " TOURNAMENT_START_DATE, TOURNAMENT_END_DATE)" +
            " VALUES(:tournamentId, :eventType, :season, :tournamentState, :tournamentName, :startDate, :endDate)" +
            " ON CONFLICT (TOURNAMENT_ID) DO UPDATE SET" +
            " EVENT_TYPE = :eventType, PGA_SEASON = :season, TOURNAMENT_STATE = :tournamentState," +
            " TOURNAMENT_NAME = :tournamentName, TOURNAMENT_START_DATE = :startDate," +
            " TOURNAMENT_END_DATE = :endDate;";

    private final String GET_WORLD_RANKINGS = "SELECT * FROM " + WORLD_RANKINGS;

    private final String GET_SCHEDULE_BY_SEASON = "SELECT * FROM " + SEASON_SCHEDULE +
            " WHERE PGA_SEASON=:season;";

    private final String GET_TOURNAMENT_BY_ID = "SELECT * FROM " + SEASON_SCHEDULE +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_ALL_TOURNAMENTS = "SELECT * FROM " + SEASON_SCHEDULE + ";";

    private final String UPDATE_TOURNAMENT_FIELD = "INSERT INTO " + TOURNAMENT_FIELD +
            " (TOURNAMENT_ID, PLAYER_ID)" +
            " VALUES(:tournamentId, :playerId)" +
            " ON CONFLICT (TOURNAMENT_ID, PLAYER_ID) DO NOTHING;";

    private final String REMOVE_FROM_TOURNAMENT_FIELD = "DELETE FROM " + TOURNAMENT_FIELD +
            " WHERE TOURNAMENT_ID=:tournamentId AND PLAYER_ID=:playerId;";

    private final String UPDATE_TOURNAMENT_COURSES = "INSERT INTO " + TOURNAMENT_COURSES +
            " (TOURNAMENT_ID, COURSE_ID, COURSE_NAME, YARDAGE, PAR, HOLES)" +
            " VALUES(:tournamentId, :courseId, :courseName, :yardage, :par, :holes)" +
            " ON CONFLICT (TOURNAMENT_ID, COURSE_ID) DO UPDATE SET" +
            " COURSE_NAME=:courseName, YARDAGE=:yardage, PAR=:par, HOLES=:holes;";

    private final String UPDATE_TOURNAMENT_ROUNDS = "INSERT INTO " + TOURNAMENT_ROUNDS +
            " (TOURNAMENT_ID, ROUND_ID, ROUND_NUMBER, STATUS)" +
            " VALUES(:tournamentId, :roundId, :roundNumber, :status)" +
            " ON CONFLICT(TOURNAMENT_ID, ROUND_ID) DO UPDATE SET" +
            " ROUND_NUMBER=:roundNumber, STATUS=:status;";

    private final String GET_TOURNAMENT_FIELD_DETAILS_BY_ID = "SELECT * FROM " + TOURNAMENT_FIELD +
            " INNER JOIN " + WORLD_RANKINGS +
            " ON " + TOURNAMENT_FIELD + ".PLAYER_ID = " + WORLD_RANKINGS + ".PLAYER_ID" +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_TOURNAMENT_FIELD_BY_ID = "SELECT * FROM " + TOURNAMENT_FIELD +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_TOURNAMENT_COURSES_BY_ID = "SELECT * FROM " + TOURNAMENT_COURSES +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_TOURNAMENT_ROUNDS_BY_ID = "SELECT * FROM " + TOURNAMENT_ROUNDS +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String UPDATE_PLAYER_ROUNDS = "INSERT INTO " + PLAYER_ROUNDS +
            " (PLAYER_ID, TOURNAMENT_ID, ROUND_ID, ROUND_NUMBER, COURSE_ID, TO_PAR, THRU," +
            " STROKES, SCORES) VALUES(:playerId, :tournamentId, :roundId, :roundNumber," +
            " :courseId, :toPar, :thru, :strokes, :scores)" +
            " ON CONFLICT (PLAYER_ID, TOURNAMENT_ID, ROUND_ID) DO UPDATE SET" +
            " ROUND_NUMBER=:roundNumber, COURSE_ID=:courseId, TO_PAR=:toPar," +
            " THRU=:thru, STROKES=:strokes, SCORES=:scores;";

    private final String GET_PLAYER_ROUNDS_BY_TOURNAMENT_ID = "SELECT * FROM " + PLAYER_ROUNDS +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    public PgaDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        MapSqlParameterSource[] params = ParamHelper.getPlayerParams(pgaPlayers);

        jdbcTemplate.batchUpdate(UPDATE_RANKINGS, params);
        logger.info("World golf rankings updated");
    }

    public void updateSeasonSchedule(List<Tournament> tournaments) {
        MapSqlParameterSource[] params = ParamHelper.getTournamentScheduleParams(tournaments);

        jdbcTemplate.batchUpdate(UPDATE_SCHEDULE, params);
        logger.info("Season schedule updated");
    }

    public List<PgaPlayer> getWorldRankings() {
        return jdbcTemplate.query(GET_WORLD_RANKINGS, new PgaPlayerMapper());
    }

    public List<Tournament> getTournamentSchedulesBySeason(int year) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("season", year);
        return jdbcTemplate.query(GET_SCHEDULE_BY_SEASON, params, new TournamentRowMapper());
    }

    public Tournament getTournamentScheduleById(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);
        return jdbcTemplate.queryForObject(GET_TOURNAMENT_BY_ID, params, new TournamentRowMapper());
    }

    public List<Tournament> getAllTournamentSchedules() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbcTemplate.query(GET_ALL_TOURNAMENTS, params, new TournamentRowMapper());
    }

    public void updateTournamentDetails(Tournament tournament) {
        MapSqlParameterSource[] fieldParams = ParamHelper.getTournamentFieldParams(tournament, tournament.getTournamentField());
        MapSqlParameterSource[] courseParams = ParamHelper.getTournamentCourseParams(tournament);
        MapSqlParameterSource[] roundParams = ParamHelper.getTournamentRoundParams(tournament);

        jdbcTemplate.batchUpdate(UPDATE_TOURNAMENT_FIELD, fieldParams);
        jdbcTemplate.batchUpdate(UPDATE_TOURNAMENT_COURSES, courseParams);
        jdbcTemplate.batchUpdate(UPDATE_TOURNAMENT_ROUNDS, roundParams);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournament.getTournamentId());
        List<PgaPlayer> tournamentField = jdbcTemplate.query(GET_TOURNAMENT_FIELD_BY_ID, params, new TournamentFieldMapper());
        List<PgaPlayer> playersToRemove = tournamentField.stream().filter(pgaPlayer -> {
            List<PgaPlayer> updatedField = tournament.getTournamentField().stream()
                    .filter(updatedPgaPlayer -> pgaPlayer.getPlayerId().equals(updatedPgaPlayer.getPlayerId()))
                    .collect(Collectors.toList());

            return updatedField.isEmpty();
        }).collect(Collectors.toList());
        MapSqlParameterSource[] removeFieldParams = ParamHelper.getTournamentFieldParams(tournament, playersToRemove);
        jdbcTemplate.batchUpdate(REMOVE_FROM_TOURNAMENT_FIELD, removeFieldParams);
    }

    public List<PgaPlayer> getTournamentField(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);
        return jdbcTemplate.query(GET_TOURNAMENT_FIELD_DETAILS_BY_ID, params, new PgaPlayerMapper());
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
