package crocker.golf.bestball.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PgaUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateService.class);

    public void process() {
        logger.info("Making call to SportsData.io for the latest data.");
    }
}
