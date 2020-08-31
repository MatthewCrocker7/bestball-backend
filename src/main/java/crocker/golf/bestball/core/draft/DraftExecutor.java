package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.domain.enums.ReleaseStatus;
import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraftExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DraftTasklet.class);

    private DraftScheduler draftScheduler;
    private DraftRepository draftRepository;
    private PgaRepository pgaRepository;

    public DraftExecutor(DraftScheduler draftScheduler, DraftRepository draftRepository) {
        this.draftScheduler = draftScheduler;
        this.draftRepository = draftRepository;
    }

    public void execute(DraftSchedule draftSchedule) {
        logger.info("Executing draft {} at {}", draftSchedule.getDraftId(), draftSchedule.getReleaseTime());
        releaseDraftSchedule(draftSchedule);

        Draft draft = draftRepository.getLatestDraftById(draftSchedule.getDraftId());

        Draft updatedDraft = initiateDraft(draft);

        draftRepository.saveDraft(updatedDraft);

        // Users will log in, see available in progress draft
        // At this point draft should have order set up, and available players
    }

    private Draft initiateDraft(Draft draft) {
        Draft updatedDraft = Draft.builder()
                .draftId(draft.getDraftId())
                .draftVersion(draft.getDraftVersion() + 1)
                .draftState(DraftState.IN_PROGRESS)
                .startTime(draft.getStartTime())
                .build();

        return updatedDraft;
    }

    private void releaseDraftSchedule(DraftSchedule draftSchedule) {
        draftScheduler.getTaskMap().remove(draftSchedule.getDraftId());

        draftRepository.updateReleaseStatus(getReleasedDraft(draftSchedule));
    }

    private DraftSchedule getReleasedDraft(DraftSchedule draftSchedule) {
        return DraftSchedule.builder()
                .draftId(draftSchedule.getDraftId())
                .releaseTime(draftSchedule.getReleaseTime())
                .releaseStatus(ReleaseStatus.RELEASED)
                .build();
    }
}
