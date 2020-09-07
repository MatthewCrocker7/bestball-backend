package crocker.golf.bestball.core.scheduler;

import crocker.golf.bestball.core.service.pga.PgaUpdateService;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

public class PgaUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateScheduler.class);

    private PgaUpdateService pgaUpdateService;

    public PgaUpdateScheduler(PgaUpdateService pgaUpdateService) {
        this.pgaUpdateService = pgaUpdateService;
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.rankings}")
    @Async
    public void updateWorldRankings() throws Exception {
        logger.info("Updating world golf rankings on thread {}", Thread.currentThread().getName());
        pgaUpdateService.processUpdateWorldRankings();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.schedule}", initialDelay = 2000)
    @Async
    public void updateSeasonSchedule() throws Exception {
        logger.info("Updating season schedule on thread {}", Thread.currentThread().getName());
        pgaUpdateService.processUpdateSeasonSchedule();
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.tournament}")
    @Async
    public void updateCurrentTournament() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        logger.info("Updating current tournament on thread {}", Thread.currentThread().getName());
        logger.info("Total amount of threads {}",  threadSet.size());
        pgaUpdateService.processUpdateCurrentTournament();
    }
}
