package crocker.golf.bestball.core.scheduler;

import crocker.golf.bestball.core.service.pga.PgaUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class PgaUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateScheduler.class);

    private PgaUpdateService pgaUpdateService;

    public PgaUpdateScheduler(PgaUpdateService pgaUpdateService) {
        this.pgaUpdateService = pgaUpdateService;
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.rankings}", initialDelay = 0)
    @Async
    public void updateWorldRankings() throws Exception {
        logger.info("Updating world golf rankings on thread {}", Thread.currentThread().getName());
        pgaUpdateService.updateWorldRankings();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.schedule}", initialDelay = 10000)
    @Async
    public void updateSeasonSchedule() throws Exception {
        logger.info("Updating season schedule on thread {}", Thread.currentThread().getName());
        pgaUpdateService.updateSeasonSchedule();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.tournament}", initialDelay = 30000)
    @Async
    public void updateTournamentDetails() {
        logger.info("Updating tournament summary on thread {}", Thread.currentThread().getName());
        pgaUpdateService.updateTournamentDetails();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.round}", initialDelay = 60000)
    @Async
    public void updateTournamentRound() {
        logger.info("Updating tournament round on thread {}", Thread.currentThread().getName());
        pgaUpdateService.updateTournamentRound();
    }
}
