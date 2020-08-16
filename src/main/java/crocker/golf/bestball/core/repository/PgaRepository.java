package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collections;
import java.util.List;

public class PgaRepository {

    private PgaDao pgaDao;

    public PgaRepository(PgaDao pgaDao) {
        this.pgaDao = pgaDao;
    }

    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        pgaDao.updateWorldRankings(pgaPlayers);
    }

    public void updateSeasonSchedule(List<Tournament> tournaments) {
        pgaDao.updateSeasonSchedule(tournaments);
    }

    @Cacheable("worldRankings")
    public List<PgaPlayer> getWorldRankings() {
        return pgaDao.getWorldRankings();
    }

    @Cacheable("tournaments")
    public List<Tournament> getCurrentSeasonTournaments() {
        return Collections.emptyList();
    }
}
