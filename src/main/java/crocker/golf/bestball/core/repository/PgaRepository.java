package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.PgaDao;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
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
            @CacheEvict(value = "allTournaments"),
            @CacheEvict(value = "inProgressTournaments")
    })
    public void updateSeasonSchedule(List<Tournament> tournaments) {
        pgaDao.updateSeasonSchedule(tournaments);
    }


    @Cacheable("tournamentsBySeason")
    public List<Tournament> getTournamentsBySeason(int year) {
        return pgaDao.getTournamentSchedulesBySeason(year);
    }

    @Cacheable(value = "tournamentById", key = "#tournamentId")
    public Tournament getTournamentById(UUID tournamentId) {
        return pgaDao.getTournamentScheduleById(tournamentId);
    }

    @Cacheable(value = "allTournaments")
    public List<Tournament> getAllTournaments () {
        return pgaDao.getAllTournamentSchedules();
    }

    @Cacheable(value = "inProgressTournaments")
    public List<Tournament> getInProgressTournaments() {
        List<Tournament> tournaments = this.getAllTournaments();
        return tournaments.stream()
                .filter(tournament -> tournament.getTournamentState() == TournamentState.IN_PROGRESS)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "tournamentField", key = "#tournament.getTournamentId()"),
            @CacheEvict(value = "tournamentCourses", key = "#tournament.getTournamentId()"),
            @CacheEvict(value = "tournamentRounds", key = "#tournament.getTournamentId()")
    })
    public void updateTournamentDetails(Tournament tournament) {
        pgaDao.updateTournamentDetails(tournament);
    }

    @Cacheable(value = "tournamentField", key = "#tournamentId")
    public List<PgaPlayer> getTournamentField(UUID tournamentId) {
        return pgaDao.getTournamentField(tournamentId);
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
