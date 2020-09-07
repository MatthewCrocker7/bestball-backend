package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.UserInfo;

import java.util.List;
import java.util.UUID;

public interface DraftDao {

    void saveDraft(Draft draft);

    void saveDraftSchedule(DraftSchedule draftSchedule);

    void updateReleaseStatus(DraftSchedule draftSchedule);

    Draft getLatestDraftById(UUID draftId);

    void saveDraftList(UUID draftId, List<PgaPlayer> pgaPlayers);

    void saveDraftOrder(UUID draftId, List<UserInfo> users);

    List<PgaPlayer> getDraftablePgaPlayersByDraftId(UUID draftId);

    List<UserInfo> getDraftOrderByDraftId(UUID draftId);
}
