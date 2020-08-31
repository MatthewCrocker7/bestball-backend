package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.core.mapper.DraftRowMapper;
import crocker.golf.bestball.core.mapper.TeamRowMapper;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TeamDao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String TEAMS = "TEAMS";

    private final String SAVE_NEW_TEAM = "INSERT INTO " + TEAMS +
            " (TEAM_ID, USER_ID, GAME_ID, DRAFT_ID, TEAM_ROLE)" +
            " VALUES(:teamId, :userId, :gameId, :draftId, :teamRole);";

    private final String GET_TEAMS_BY_USER_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId;";

    public TeamDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveTeam(Team team) {
        MapSqlParameterSource params = getTeamParams(team);
        jdbcTemplate.update(SAVE_NEW_TEAM, params);
    }

    public List<Team> getTeamsByUserId(UUID userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);

        return jdbcTemplate.query(GET_TEAMS_BY_USER_ID, params, new TeamRowMapper());
    }

    private MapSqlParameterSource getTeamParams(Team team) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("teamId", team.getTeamId());
        params.addValue("userId", team.getUserId());
        params.addValue("gameId", team.getGameId());
        params.addValue("draftId", team.getDraftId());
        params.addValue("teamRole", team.getTeamRole().name());

        return params;
    }
}
