package crocker.golf.bestball.core.replay;

import crocker.golf.bestball.core.service.PgaUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class PgaUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateScheduler.class);

    private PgaUpdateService pgaUpdateService;

    public PgaUpdateScheduler(PgaUpdateService pgaUpdateService) {
        this.pgaUpdateService = pgaUpdateService;
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate}")
    public void update() {
        logger.info("Updating PGA scores.");
        pgaUpdateService.process();
    }
}
