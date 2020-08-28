package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.DraftDao;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DraftRepository {

    private DraftDao draftDao;

    public DraftRepository(DraftDao draftDao) {
        this.draftDao = draftDao;
    }

    public void saveDraft(Draft draft) {
        draftDao.saveDraft(draft);
    }

    public void saveDraftSchedule(DraftSchedule draftSchedule) {
        draftDao.saveDraftSchedule(draftSchedule);
    }

    public void updateReleaseStatus(DraftSchedule draftSchedule) {
        draftDao.updateReleaseStatus(draftSchedule);
    }

    public Draft getLatestDraftById(UUID draftId) {
        return draftDao.getLatestDraftById(draftId);
    }

    public List<DraftSchedule> getDraftSchedules() { return Collections.emptyList(); }
}
