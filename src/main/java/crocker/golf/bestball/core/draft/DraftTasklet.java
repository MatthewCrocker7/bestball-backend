package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraftTasklet implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DraftTasklet.class);

    private DraftExecutor draftExecutor;
    private DraftSchedule draftSchedule;

    public DraftTasklet(DraftExecutor draftExecutor) {
        this.draftExecutor = draftExecutor;
    }

    public DraftTasklet withDraftSchedule(DraftSchedule draftSchedule) {
        this.draftSchedule = draftSchedule;
        return this;
    }

    @Override
    public void run() {

    }
}
