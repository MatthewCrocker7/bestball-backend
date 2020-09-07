package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.DraftDao;
import crocker.golf.bestball.core.dao.DraftDaoImpl;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.UserInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public void saveDraftList(UUID draftId, List<PgaPlayer> pgaPlayers) {
        draftDao.saveDraftList(draftId, pgaPlayers);
    }

    public void saveDraftOrder(UUID draftId, List<UserInfo> users) {
        draftDao.saveDraftOrder(draftId, users);
    }

    public Draft getLatestDraftById(UUID draftId) {
        return draftDao.getLatestDraftById(draftId);
    }

    public List<DraftSchedule> getDraftSchedules() { return Collections.emptyList(); }

    public List<PgaPlayer> getDraftablePgaPlayersByDraftId(UUID draftId) {
        return draftDao.getDraftablePgaPlayersByDraftId(draftId);
    }

    public Map<Integer, UserInfo> getDraftOrderByDraftId(UUID draftId) {
        List<UserInfo> users = draftDao.getDraftOrderByDraftId(draftId);
        return users.stream()
            .collect(Collectors.toMap(UserInfo::getPickNumber, user -> user));
    }
}
