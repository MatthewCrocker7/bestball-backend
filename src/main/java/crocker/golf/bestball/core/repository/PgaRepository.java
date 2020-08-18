package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collections;
import java.util.List;

public class PgaRepository {

    private PgaDao pgaDao;

    public PgaRepository(PgaDao pgaDao) {
        this.pgaDao = pgaDao;
    }

    @CacheEvict(value = "worldRankings", allEntries = true)
    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        pgaDao.updateWorldRankings(pgaPlayers);
    }

    @CacheEvict(value = "tournamentsBySeason", allEntries = true)
    public void updateSeasonSchedule(List<Tournament> tournaments) {
        pgaDao.updateSeasonSchedule(tournaments);
    }

    @Cacheable("worldRankings")
    public List<PgaPlayer> getWorldRankings() {
        return pgaDao.getWorldRankings();
    }

    @Cacheable("tournamentsBySeason")
    public List<Tournament> getTournamentsBySeason(int year) {
        return pgaDao.getTournamentsBySeason(year);
    }

    @Cacheable(value = "tournamentById", key = "#tournamentId")
    public Tournament getTournamentById(String tournamentId) {
        return pgaDao.getTournamentById(tournamentId);
    }
}
