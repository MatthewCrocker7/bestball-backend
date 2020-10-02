package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PgaRepository {

    private PgaDao pgaDao;

    public PgaRepository(PgaDao pgaDao) {
        this.pgaDao = pgaDao;
    }

    @CacheEvict(value = "worldRankings", allEntries = true)
    public void updateWorldRankings(List<PgaPlayer> pgaPlayers) {
        pgaDao.updateWorldRankings(pgaPlayers);
    }

    @Cacheable("worldRankings")
    public List<PgaPlayer> getWorldRankings() {
        return pgaDao.getWorldRankings();
    }

    public Map<UUID, PgaPlayer> getWorldRankingsAsMap() {
        return this.getWorldRankings().stream()
                .collect(Collectors.toMap(PgaPlayer::getPlayerId, pgaPlayer -> pgaPlayer));
    }

    @Caching(evict = {
            @CacheEvict(value = "tournamentsBySeason", allEntries = true),
            @CacheEvict(value = "tournamentById", allEntries = true),
            @CacheEvict(value = "allTournaments")
    })
    public void updateSeasonSchedule(List<Tournament> tournaments) {
        pgaDao.updateSeasonSchedule(tournaments);
    }


    @Cacheable("tournamentsBySeason")
    public List<Tournament> getTournamentsBySeason(int year) {
        return pgaDao.getTournamentsBySeason(year);
    }

    @Cacheable(value = "tournamentById", key = "#tournamentId")
    public Tournament getTournamentById(UUID tournamentId) {
        return pgaDao.getTournamentById(tournamentId);
    }

    @Cacheable(value = "allTournaments")
    public List<Tournament> getAllTournaments () {
        return pgaDao.getAllTournaments();
    }

    @Caching(evict = {
            @CacheEvict(value = "tournamentCourses", key = "#tournamentSummary.getTournamentId()"),
            @CacheEvict(value = "tournamentRounds", key = "#tournamentSummary.getTournamentId()")
    })
    public void updateTournamentSummary(TournamentSummary tournamentSummary) {
        pgaDao.updateTournamentSummary(tournamentSummary);
    }

    @Cacheable(value = "tournamentCourses", key = "#tournamentId")
    public List<TournamentCourse> getTournamentCourses(UUID tournamentId) {
        return pgaDao.getTournamentCourses(tournamentId);
    }

    @Cacheable(value = "tournamentRounds", key = "#tournamentId")
    public List<TournamentRound> getTournamentRounds(UUID tournamentId) {
        return pgaDao.getTournamentRounds(tournamentId);
    }

    public void updatePlayerRounds(List<PlayerRound> playerRounds) {
        pgaDao.updatePlayerRounds(playerRounds);
    }

    public List<PlayerRound> getPlayerRoundsByTournamentId(UUID tournamentId) {
        return pgaDao.getPlayerRoundsByTournamentId(tournamentId);
    }
}
