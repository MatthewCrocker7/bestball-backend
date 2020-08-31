package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.core.mapper.DraftRowMapper;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.UUID;

public class DraftDao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String DRAFTS = "DRAFTS";
    private final String DRAFT_SCHEDULES = "DRAFT_SCHEDULES";

    private final String SAVE_NEW_DRAFT = "INSERT INTO " + DRAFTS +
            " (DRAFT_ID, DRAFT_STATE, DRAFT_VERSION, DRAFT_TIME)" +
            " VALUES(:draftId, :draftState, :draftVersion, :draftTime);";

    private final String SAVE_NEW_DRAFT_SCHEDULE = "INSERT INTO " + DRAFT_SCHEDULES +
            " (DRAFT_ID, RELEASE_STATUS, RELEASE_TIME)" +
            " VALUES(:draftId, :releaseStatus, :releaseTime);";

    private final String UPDATE_RELEASE_STATUS = "UPDATE " + DRAFT_SCHEDULES +
            " SET RELEASE_STATUS = :releaseStatus" +
            " WHERE DRAFT_ID = :draftId;";

    private final String GET_LATEST_DRAFT_BY_ID = "SELECT * FROM " + DRAFTS +
            " WHERE (DRAFT_ID, DRAFT_VERSION) IN" +
            " (SELECT DRAFT_ID, MAX(DRAFT_VERSION) FROM " + DRAFTS +
            " WHERE DRAFT_ID=:draftId);";

    public DraftDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveDraft(Draft draft) {
        MapSqlParameterSource params = getDraftParams(draft);
        jdbcTemplate.update(SAVE_NEW_DRAFT, params);
    }

    public void saveDraftSchedule(DraftSchedule draftSchedule) {
        MapSqlParameterSource params = getDraftScheduleParams(draftSchedule);
        jdbcTemplate.update(SAVE_NEW_DRAFT_SCHEDULE, params);
    }

    public void updateReleaseStatus(DraftSchedule draftSchedule) {
        MapSqlParameterSource params = getDraftScheduleParams(draftSchedule);
        jdbcTemplate.update(UPDATE_RELEASE_STATUS, params);
    }

    public Draft getLatestDraftById(UUID draftId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);

        return jdbcTemplate.queryForObject(GET_LATEST_DRAFT_BY_ID, params, new DraftRowMapper());
    }

    private MapSqlParameterSource getDraftParams(Draft draft) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draft.getDraftId());
        params.addValue("draftState", draft.getDraftState().name());
        params.addValue("draftVersion", draft.getDraftVersion());
        params.addValue("draftTime", draft.getStartTime());

        return params;
    }

    private MapSqlParameterSource getDraftScheduleParams(DraftSchedule draftSchedule) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftSchedule.getDraftId());
        params.addValue("releaseStatus", draftSchedule.getReleaseStatus().name());
        params.addValue("releaseTime", draftSchedule.getReleaseTime());

        return params;
    }
}
