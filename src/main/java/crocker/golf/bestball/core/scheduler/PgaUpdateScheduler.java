package crocker.golf.bestball.core.scheduler;

import crocker.golf.bestball.core.service.pga.PgaUpdateService;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class PgaUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateScheduler.class);

    private PgaUpdateService pgaUpdateService;

    public PgaUpdateScheduler(PgaUpdateService pgaUpdateService) {
        this.pgaUpdateService = pgaUpdateService;
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.rankings}")
    public void updateWorldRankings() throws ExternalAPIException {
        logger.info("Updating world golf rankings");
        pgaUpdateService.processUpdateWorldRankings();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.schedule}")
    public void updateSeasonSchedule() throws ExternalAPIException {
        logger.info("Updating season schedule");
        pgaUpdateService.processUpdateSeasonSchedule();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.tournament}")
    public void updateCurrentTournament() {
        logger.info("Updating current tournament");
        pgaUpdateService.processUpdateCurrentTournament();
    }
}
