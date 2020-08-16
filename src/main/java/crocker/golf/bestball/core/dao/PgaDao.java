package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PgaDao {

    private static final Logger logger = LoggerFactory.getLogger(PgaDao.class);

    private JdbcTemplate jdbcTemplate;

    private final String WORLD_RANKINGS = "WORLD_RANKINGS";
    private final String SEASON_SCHEDULE = "SEASON_SCHEDULE";

    private final String DELETE_RANKINGS = "DELETE FROM " + WORLD_RANKINGS + ";";
    private final String DELETE_SCHEDULE = "DELETE FROM " + SEASON_SCHEDULE + ";";

    private final String UPDATE_RANKINGS = "INSERT INTO " + WORLD_RANKINGS +
            " (PLAYER_ID, PLAYER_RANK, PLAYER_NAME)" +
            " VALUES(?, ?, ?);";

    private final String UPDATE_SCHEDULE = "INSERT INTO " + SEASON_SCHEDULE +
            " (TOURNAMENT_ID, EVENT_TYPE, SEASON, STATE, NAME, START_DATE, END_DATE)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?);";


    public PgaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        logger.info("Updating world rankings");

        List<Object[]> params = getPlayerParams(pgaPlayers);

        jdbcTemplate.execute(DELETE_RANKINGS);
        jdbcTemplate.batchUpdate(UPDATE_RANKINGS, params);
    }

    public void updateSeasonSchedule(List<Tournament> tournaments) {
        logger.info("Updating season schedule");

        List<Object[]> params = getTournamentParams(tournaments);

        jdbcTemplate.execute(DELETE_SCHEDULE);
        jdbcTemplate.batchUpdate(UPDATE_SCHEDULE, params);
    }

    public List<PgaPlayer> getWorldRankings() {
        return Collections.emptyList();
    }

    public List<Tournament> getSeasonSchedule() { return Collections.emptyList(); }

    private List<Object[]> getPlayerParams(List<PgaPlayer> pgaPlayers) {
        return  pgaPlayers.stream().map(pgaPlayer -> new Object[] {
                pgaPlayer.getPlayerId(),
                pgaPlayer.getRank(),
                pgaPlayer.getPlayerName()
        }).collect(Collectors.toList());
    }

    private List<Object[]> getTournamentParams(List<Tournament> tournaments) {
        return tournaments.stream().map(tournament -> new Object[] {
                tournament.getSportsRadarTournamentId(),
                tournament.getEventType().name(),
                tournament.getSeason(),
                tournament.getTournamentState().name(),
                tournament.getName(),
                tournament.getStartDate(),
                tournament.getEndDate(),

        }).collect(Collectors.toList());
    }

}
