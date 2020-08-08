package crocker.golf.bestball.core.service.pga;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsDataService;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.WorldGolfRankings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PgaUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateService.class);

    private SportsDataService sportsDataService;
    private PgaRepository pgaRepository;

    public PgaUpdateService(SportsDataService sportsDataService, PgaRepository pgaRepository) {
        this.sportsDataService = sportsDataService;
        this.pgaRepository = pgaRepository;
    }

    // update world golf rankings with /PlayerSeasonStats (presorted list 1 through x)
    // update tournament schedule by season with /tournaments/season(2020)
    // update individual player scores with /PlayerTournamentStatsByPlayer
    // update leaderboard with /Leaderboard

    public void processUpdateWorldRankings() throws ExternalAPIException {
        logger.info("Calling api for world golf rankings");

        List<PgaPlayer> pgaPlayers = sportsDataService.getWorldRankings();

        pgaRepository.updateWorldRankings(pgaPlayers);

        logger.info("World golf rankings updated with top {} players", pgaPlayers.size());
    }

    public void processUpdateSeasonSchedule() {
        logger.info("Calling api for current season schedule");
    }

    public void processUpdateCurrentTournament() {
        logger.info("Calling api for currrent tournament leaderboard");
        // call api for leaderboard
        // then call api X number of times for each player on board

    }

    private Map<Integer, PgaPlayer> mapPlayerListToRankings(List<PgaPlayer> pgaPlayers) {
        return pgaPlayers.stream()
                .collect(Collectors.toMap(PgaPlayer::getRank, player -> player));
    }
}
