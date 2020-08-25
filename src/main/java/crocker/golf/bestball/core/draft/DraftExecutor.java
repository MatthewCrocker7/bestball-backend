package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraftExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DraftTasklet.class);

    private DraftScheduler draftScheduler;

    public DraftExecutor(DraftScheduler draftScheduler) {
        this.draftScheduler = draftScheduler;
    }

    public void execute(DraftSchedule draftSchedule) {
        logger.info("Executing draft {} at {}", draftSchedule.getDraftId(), draftSchedule.getReleaseTime());
        draftScheduler.getTaskMap().remove(draftSchedule.getDraftId());
    }
}
