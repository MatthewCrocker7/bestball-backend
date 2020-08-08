package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.pga.PgaPlayer;
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

    private final String DELETE_RANKINGS = "DELETE FROM " + WORLD_RANKINGS + ";";

    private final String UPDATE_RANKINGS = "INSERT INTO " + WORLD_RANKINGS +
            " (PLAYER_RANK, PLAYER_ID, PLAYER_NAME)" +
            " VALUES(?, ?, ?);";


    public PgaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        logger.info("Updating world rankings");

        List<Object[]> params = getParams(pgaPlayers);

        jdbcTemplate.execute(DELETE_RANKINGS);
        jdbcTemplate.batchUpdate(UPDATE_RANKINGS, params);
    }

    public List<PgaPlayer> getWorldRankings() {
        return Collections.emptyList();
    }

    public List<Object[]> getParams(List<PgaPlayer> pgaPlayers) {
        return  pgaPlayers.stream().map(pgaPlayer -> new Object[] {
                pgaPlayer.getRank(),
                pgaPlayer.getPlayerId(),
                pgaPlayer.getPlayerName()
        }).collect(Collectors.toList());
    }

}
