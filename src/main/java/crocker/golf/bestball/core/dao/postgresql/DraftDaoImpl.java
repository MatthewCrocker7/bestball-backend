package crocker.golf.bestball.core.dao.postgresql;

import crocker.golf.bestball.core.dao.DraftDao;
import crocker.golf.bestball.core.mapper.DraftRowMapper;
import crocker.golf.bestball.core.mapper.DraftScheduleRowMapper;
import crocker.golf.bestball.core.mapper.PgaPlayerMapper;
import crocker.golf.bestball.core.mapper.UserInfoRowMapper;
import crocker.golf.bestball.domain.enums.game.ReleaseStatus;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class DraftDaoImpl implements DraftDao {

    private static final Logger logger = LoggerFactory.getLogger(DraftDaoImpl.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String DRAFTS = "DRAFTS";
    private final String DRAFT_SCHEDULES = "DRAFT_SCHEDULES";
    private final String DRAFT_PGA_PLAYERS = "DRAFT_PGA_PLAYERS";
    private final String DRAFT_ORDER = "DRAFT_ORDER";

    private final String SAVE_NEW_DRAFT = "INSERT INTO " + DRAFTS +
            " (DRAFT_ID, DRAFT_STATE, DRAFT_VERSION, DRAFT_TIME, CURRENT_PICK)" +
            " VALUES(:draftId, :draftState, :draftVersion, :draftTime, :currentPick);";

    private final String SAVE_NEW_DRAFT_SCHEDULE = "INSERT INTO " + DRAFT_SCHEDULES +
            " (DRAFT_ID, RELEASE_STATUS, RELEASE_TIME)" +
            " VALUES(:draftId, :releaseStatus, :releaseTime);";

    private final String UPDATE_RELEASE_STATUS = "UPDATE " + DRAFT_SCHEDULES +
            " SET RELEASE_STATUS = :releaseStatus" +
            " WHERE DRAFT_ID = :draftId;";

    private final String SAVE_DRAFTABLE_PGA_PLAYERS = "INSERT INTO " + DRAFT_PGA_PLAYERS +
            " (DRAFT_ID, PLAYER_ID, PLAYER_RANK, PLAYER_NAME, DRAFTED)" +
            " VALUES(:draftId, :playerId, :playerRank, :playerName, :drafted);";

    private final String SAVE_DRAFT_ORDER = "INSERT INTO " + DRAFT_ORDER +
            " (DRAFT_ID, USER_ID, PICK_NUMBER, USER_NAME, EMAIL)" +
            " VALUES(:draftId, :userId, :pickNumber, :userName, :email);";

    private final String DRAFT_PLAYER = "UPDATE " + DRAFT_PGA_PLAYERS +
            " SET DRAFTED=:drafted" +
            " WHERE DRAFT_ID=:draftId AND PLAYER_ID=:playerId;";

    private final String GET_LATEST_DRAFT_BY_ID = "SELECT * FROM " + DRAFTS +
            " WHERE (DRAFT_ID, DRAFT_VERSION) IN" +
            " (SELECT DRAFT_ID, MAX(DRAFT_VERSION) FROM " + DRAFTS +
            " WHERE DRAFT_ID=:draftId GROUP BY DRAFT_ID);";

    private final String GET_DRAFT_SCHEDULES_BY_RELEASE_STATUS = "SELECT * FROM " + DRAFT_SCHEDULES +
            " WHERE RELEASE_STATUS = :releaseStatus;";

    private final String GET_DRAFTABLE_PGA_PLAYERS = "SELECT * FROM " + DRAFT_PGA_PLAYERS +
            " WHERE DRAFT_ID=:draftId AND DRAFTED=:drafted;";

    private final String GET_DRAFT_ORDER_BY_DRAFT_ID = "SELECT * FROM " + DRAFT_ORDER +
            " WHERE DRAFT_ID=:draftId;";

    private final String GET_PLAYER_BY_ID  = "SELECT * FROM " + DRAFT_PGA_PLAYERS +
            " WHERE DRAFT_ID=:draftId AND PLAYER_ID=:playerId;";

    public DraftDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
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

    public List<DraftSchedule> getDraftSchedulesByReleaseStatus(ReleaseStatus releaseStatus) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("releaseStatus", releaseStatus.name());

        return jdbcTemplate.query(GET_DRAFT_SCHEDULES_BY_RELEASE_STATUS, params, new DraftScheduleRowMapper());
    }

    public void saveDraftList(UUID draftId, List<PgaPlayer> pgaPlayers) {
        logger.info("Finalizing pga players for draft {}", draftId);

        MapSqlParameterSource[] params = getNewDraftPgaPlayers(draftId, pgaPlayers);
        jdbcTemplate.batchUpdate(SAVE_DRAFTABLE_PGA_PLAYERS, params);
    }

    public void saveDraftOrder(UUID draftId, List<UserInfo> users) {
        logger.info("Finalizing draft order for draft {}", draftId);

        MapSqlParameterSource[] params = getNewDraftOrder(draftId, users);
        jdbcTemplate.batchUpdate(SAVE_DRAFT_ORDER, params);
    }

    public void draftPlayer(UUID draftId, PgaPlayer pgaPlayer) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);
        params.addValue("playerId", pgaPlayer.getPlayerId());
        params.addValue("drafted", true);

        jdbcTemplate.update(DRAFT_PLAYER, params);
    }

    public List<PgaPlayer> getDraftablePgaPlayersByDraftId(UUID draftId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);
        params.addValue("drafted", false);

        return jdbcTemplate.query(GET_DRAFTABLE_PGA_PLAYERS, params, new PgaPlayerMapper());
    }

    public List<UserInfo> getDraftOrderByDraftId(UUID draftId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);

        return jdbcTemplate.query(GET_DRAFT_ORDER_BY_DRAFT_ID, params, new UserInfoRowMapper());
    }

    public PgaPlayer getPgaPlayerById(UUID draftId, UUID playerId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);
        params.addValue("playerId", playerId);
        return jdbcTemplate.queryForObject(GET_PLAYER_BY_ID, params, new PgaPlayerMapper());
    }

    private MapSqlParameterSource getDraftParams(Draft draft) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draft.getDraftId());
        params.addValue("draftState", draft.getDraftState().name());
        params.addValue("draftVersion", draft.getDraftVersion());
        params.addValue("draftTime", draft.getStartTime());
        params.addValue("currentPick", draft.getCurrentPick());

        return params;
    }

    private MapSqlParameterSource getDraftScheduleParams(DraftSchedule draftSchedule) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftSchedule.getDraftId());
        params.addValue("releaseStatus", draftSchedule.getReleaseStatus().name());
        params.addValue("releaseTime", draftSchedule.getReleaseTime());

        return params;
    }

    private MapSqlParameterSource[] getNewDraftPgaPlayers(UUID draftId, List<PgaPlayer> pgaPlayers) {
        return pgaPlayers.stream().map(pgaPlayer -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("draftId", draftId);
            params.addValue("playerId", pgaPlayer.getPlayerId());
            params.addValue("playerRank", pgaPlayer.getRank());
            params.addValue("playerName", pgaPlayer.getPlayerName());
            params.addValue("drafted", false);

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource[] getNewDraftOrder(UUID draftId, List<UserInfo> users) {
        return users.stream().map(user -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("draftId", draftId);
            params.addValue("userId", user.getUserId());
            params.addValue("pickNumber", user.getPickNumber());
            params.addValue("userName", user.getUserName());
            params.addValue("email", user.getEmail());

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }
}
