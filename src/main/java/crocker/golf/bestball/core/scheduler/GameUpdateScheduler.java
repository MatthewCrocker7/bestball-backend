package crocker.golf.bestball.core.scheduler;

import crocker.golf.bestball.core.service.game.GameManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class GameUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(GameUpdateScheduler.class);

    private GameManagerService gameManagerService;

    public GameUpdateScheduler(GameManagerService gameManagerService){
        this.gameManagerService = gameManagerService;
    }

    @Scheduled(fixedDelayString = "${golf.pga.update.rate.round}", initialDelay = 600000000)
    @Async
    public void updateTeamScores() {
        logger.info("Updating all active games on thread {}", Thread.currentThread().getName());
        gameManagerService.updateGames();
    }
}
