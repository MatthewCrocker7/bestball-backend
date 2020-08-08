package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public class PgaRepository {

    private PgaDao pgaDao;

    public PgaRepository(PgaDao pgaDao) {
        this.pgaDao = pgaDao;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        pgaDao.updateWorldRankings(pgaPlayers);
    }

    @Cacheable("worldRankings")
    public List<PgaPlayer> getWorldRankings() {
        return pgaDao.getWorldRankings();
    }
}
