package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.springframework.jdbc.core.JdbcTemplate;

public class DraftDao {

    private JdbcTemplate jdbcTemplate;

    private final String DRAFTS = "DRAFTS";
    private final String DRAFT_SCHEDULES = "DRAFT_SCHEDULES";

    private final String SAVE_NEW_DRAFT = "INSERT INTO " + DRAFTS +
            " (DRAFT_ID, DRAFT_STATE, DRAFT_VERSION)" +
            " VALUES(?, ?, ?);";

    private final String SAVE_NEW_DRAFT_SCHEDULE = "INSERT INTO " + DRAFT_SCHEDULES +
            " (DRAFT_ID, RELEASE_STATUS, RELEASE_TIME)" +
            " VALUES(?, ?, ?);";

    public DraftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveDraft(Draft draft) {
        Object[] params = getDraftParams(draft);
        jdbcTemplate.update(SAVE_NEW_DRAFT, params);
    }

    public void saveDraftSchedule(DraftSchedule draftSchedule) {
        Object[] params = getDraftScheduleParams(draftSchedule);
        jdbcTemplate.update(SAVE_NEW_DRAFT_SCHEDULE, params);
    }

    private Object[] getDraftParams(Draft draft) {
        return new Object[] {
                draft.getDraftId(),
                draft.getDraftState().name(),
                draft.getDraftVersion()
        };
    }

    private Object[] getDraftScheduleParams(DraftSchedule draftSchedule) {
        return new Object[] {
                draftSchedule.getDraftId(),
                draftSchedule.getReleaseStatus().name(),
                draftSchedule.getReleaseTime()
        };
    }
}
