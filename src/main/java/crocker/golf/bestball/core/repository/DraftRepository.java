package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.DraftDao;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;

import java.util.Collections;
import java.util.List;

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

    public List<DraftSchedule> getDraftSchedules() { return Collections.emptyList(); }
}
