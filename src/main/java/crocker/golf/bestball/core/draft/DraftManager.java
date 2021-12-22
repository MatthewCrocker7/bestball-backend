package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.domain.enums.game.ReleaseStatus;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DraftManager {

    private static final Logger logger = LoggerFactory.getLogger(DraftScheduler.class);

    private final DraftScheduler draftScheduler;
    private final DraftRepository draftRepository;

    public DraftManager(DraftScheduler draftScheduler, DraftRepository draftRepository) {
        this.draftScheduler = draftScheduler;
        this.draftRepository = draftRepository;
    }

    public void scheduleDraft(Draft draft) {
        //todo: persist draft DRAFT and DRAFT_SCHEDULES table
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
