package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.domain.enums.ReleaseStatus;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraftManager {

    private static final Logger logger = LoggerFactory.getLogger(DraftScheduler.class);

    private DraftScheduler draftScheduler;
    private DraftRepository draftRepository;

    public DraftManager(DraftScheduler draftScheduler, DraftRepository draftRepository) {
        this.draftScheduler = draftScheduler;
        this.draftRepository = draftRepository;
    }

    public void scheduleDraft(Draft draft) {
        // persist draft DRAFT and DRAFT_SCHEDULES table
        // use in try catch, on fail replay
        draftRepository.saveDraft(draft);

        DraftSchedule draftSchedule = getDraftSchedule(draft);
        draftRepository.saveDraftSchedule(draftSchedule);
        draftScheduler.schedule(draftSchedule);
    }

    private DraftSchedule getDraftSchedule(Draft draft) {
        return DraftSchedule.builder()
                .draftId(draft.getDraftId())
                .releaseStatus(ReleaseStatus.NOT_RELEASED)
                .releaseTime(draft.getStartTime())
                .build();
    }

}
