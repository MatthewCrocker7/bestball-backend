package crocker.golf.bestball.core.service.pga;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class PgaUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateService.class);

    private SportsApiService sportsApiService;
    private PgaRepository pgaRepository;

    public PgaUpdateService(SportsApiService sportsApiService, PgaRepository pgaRepository) {
        this.sportsApiService = sportsApiService;
        this.pgaRepository = pgaRepository;
    }

    // update world golf rankings with /PlayerSeasonStats (presorted list 1 through x)
    // update tournament schedule by season with /tournaments/season(2020)
    // update individual player scores with /PlayerTournamentStatsByPlayer
    // update leaderboard with /Leaderboard

    public void processUpdateWorldRankings() throws Exception {
        logger.info("Calling api for world golf rankings");

        Future<List<PgaPlayer>> futurePgaPlayers = sportsApiService.getWorldRankings();
        List<PgaPlayer> pgaPlayers = futurePgaPlayers.get();

        pgaRepository.updateWorldRankings(pgaPlayers);

        logger.info("World golf rankings updated with top {} players", pgaPlayers.size());
    }

    public void processUpdateSeasonSchedule() throws Exception {
        logger.info("Calling api for current season schedule");

        Future<List<Tournament>> futureTournaments = sportsApiService.getSeasonSchedule();
        List<Tournament> tournaments = futureTournaments.get();

        pgaRepository.updateSeasonSchedule(tournaments);
    }

    public void processUpdateCurrentTournament() {
        logger.info("Calling api for currrent tournament leaderboard");
        // call api for leaderboard
        // then call api X number of times for each player on board

    }
}
