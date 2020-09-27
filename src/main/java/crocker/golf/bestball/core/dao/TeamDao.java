package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.core.mapper.game.TeamRowMapper;
import crocker.golf.bestball.domain.game.Team;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class TeamDao {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private TeamRowMapper teamRowMapper;

    private final String TEAMS = "TEAMS";

    private final String SAVE_NEW_TEAM = "INSERT INTO " + TEAMS +
            " (TEAM_ID, USER_ID, GAME_ID, DRAFT_ID, TEAM_ROLE)" +
            " VALUES(:teamId, :userId, :gameId, :draftId, :teamRole);";

    private final String UPDATE_TEAM = "UPDATE " + TEAMS +
            " SET PLAYER_ONE_ID = :playerOneId, PLAYER_TWO_ID = :playerTwoId," +
            " PLAYER_THREE_ID = :playerThreeId, PLAYER_FOUR_ID = :playerFourId" +
            " WHERE TEAM_ID = :teamId;";

    private final String GET_TEAMS_BY_USER_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId;";

    private final String GET_TEAMS_BY_DRAFT_ID = "SELECT * FROM " + TEAMS +
            " WHERE DRAFT_ID=:draftId;";

    private final String GET_TEAM_BY_USER_AND_DRAFT_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId AND DRAFT_ID=:draftId;";

    private final String GET_TEAM_BY_USER_AND_GAME_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId AND GAME_ID=:gameId;";

    public TeamDao(NamedParameterJdbcTemplate jdbcTemplate, TeamRowMapper teamRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamRowMapper = teamRowMapper;
    }

    public void saveTeam(Team team) {
        MapSqlParameterSource params = getNewTeamParams(team);
        jdbcTemplate.update(SAVE_NEW_TEAM, params);
    }

    public void updateTeam(Team team) {
        MapSqlParameterSource params = getUpdateTeamParams(team);
        jdbcTemplate.update(UPDATE_TEAM, params);
    }

    public List<Team> getTeamsByUserId(UUID userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);

        return jdbcTemplate.query(GET_TEAMS_BY_USER_ID, params, teamRowMapper);
    }

    public List<Team> getTeamsByDraftId(UUID draftId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("draftId", draftId);

        return jdbcTemplate.query(GET_TEAMS_BY_DRAFT_ID, params, teamRowMapper);
    }

    public Team getTeamByUserAndDraftId(UUID userId, UUID draftId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("draftId", draftId);

        return jdbcTemplate.queryForObject(GET_TEAM_BY_USER_AND_DRAFT_ID, params, teamRowMapper);
    }

    public Team getTeamByUserAndGameId(UUID userId, UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("gameId", gameId);

        return jdbcTemplate.queryForObject(GET_TEAM_BY_USER_AND_GAME_ID, params, teamRowMapper);
    }

    private MapSqlParameterSource getNewTeamParams(Team team) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("teamId", team.getTeamId());
        params.addValue("userId", team.getUserId());
        params.addValue("gameId", team.getGameId());
        params.addValue("draftId", team.getDraftId());
        params.addValue("teamRole", team.getTeamRole().name());

        return params;
    }

    private MapSqlParameterSource getUpdateTeamParams(Team team) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("teamId", team.getTeamId());

        params.addValue("playerOneId", team.getGolferOne() != null ? team.getGolferOne().getPlayerId() : null);
        params.addValue("playerTwoId", team.getGolferTwo() != null ? team.getGolferTwo().getPlayerId() : null);
        params.addValue("playerThreeId", team.getGolferThree() != null ? team.getGolferThree().getPlayerId() : null);
        params.addValue("playerFourId", team.getGolferFour() != null ? team.getGolferFour().getPlayerId() : null);

        return params;
    }
}
