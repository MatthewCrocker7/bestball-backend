package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

public class DraftScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DraftScheduler.class);

    private TaskScheduler draftTaskScheduler;
    private DraftRepository draftRepository;

    public DraftScheduler(TaskScheduler draftTaskScheduler, DraftRepository draftRepository) {
        this.draftTaskScheduler = draftTaskScheduler;
        this.draftRepository = draftRepository;
    }

    public void schedule(DraftSchedule draftSchedule) {
        execute(draftSchedule);
    }

    public void warmUpDraftSchedules() {
        logger.info("Warming up existing draft schedules");
        draftRepository.getDraftSchedules().forEach(this::schedule);
    }

    private void execute(DraftSchedule draftSchedule) {
        logger.info("Scheduling draft {}", draftSchedule.getDraftId());


    }

}
