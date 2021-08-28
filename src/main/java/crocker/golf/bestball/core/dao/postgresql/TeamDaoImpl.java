package crocker.golf.bestball.core.dao.postgresql;

import crocker.golf.bestball.core.dao.ParamHelper;
import crocker.golf.bestball.core.dao.TeamDao;
import crocker.golf.bestball.core.mapper.game.TeamRoundMapper;
import crocker.golf.bestball.core.mapper.game.TeamRowMapper;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.round.TeamRound;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TeamDaoImpl implements TeamDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TeamRowMapper teamRowMapper;

    private final String TEAMS = "TEAMS";
    private final String TEAM_ROUNDS = "TEAM_ROUNDS";

    private final String SAVE_NEW_TEAM = "INSERT INTO " + TEAMS +
            " (TEAM_ID, USER_ID, GAME_ID, DRAFT_ID, TOURNAMENT_ID, TEAM_ROLE)" +
            " VALUES(:teamId, :userId, :gameId, :draftId, :tournamentId, :teamRole);";

    private final String UPDATE_TEAM = "INSERT INTO " + TEAMS +
            " (TEAM_ID, USER_ID, GAME_ID, DRAFT_ID, TOURNAMENT_ID, TEAM_ROLE, DRAFT_PICK," +
            " PLAYER_ONE_ID, PLAYER_TWO_ID, PLAYER_THREE_ID, PLAYER_FOUR_ID," +
            " TO_PAR, TOTAL_STROKES)" +
            " VALUES(:teamId, :userId, :gameId, :draftId, :tournamentId, :teamRole," +
            " :draftPick, :playerOneId, :playerTwoId, :playerThreeId, :playerFourId," +
            " :toPar, :totalStrokes) ON CONFLICT (TEAM_ID, USER_ID, GAME_ID, DRAFT_ID, TOURNAMENT_ID)" +
            " DO UPDATE SET TEAM_ROLE=:teamRole, DRAFT_PICK=:draftPick, PLAYER_ONE_ID=:playerOneId," +
            " PLAYER_TWO_ID=:playerTwoId, PLAYER_THREE_ID=:playerThreeId, PLAYER_FOUR_ID=:playerFourId," +
            " TO_PAR=:toPar, TOTAL_STROKES=:totalStrokes;";

    private final String DELETE_TEAMS_BY_GAME_ID = "DELETE FROM " + TEAMS +
            " WHERE GAME_ID=:gameId;";

    private final String DELETE_TEAM_ROUNDS_BY_GAME_ID = "DELETE FROM " + TEAMS +
            " WHERE GAME_ID=:gameId;";

    private final String GET_TEAMS_BY_USER_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId;";

    private final String GET_TEAMS_BY_DRAFT_ID = "SELECT * FROM " + TEAMS +
            " WHERE DRAFT_ID=:draftId;";

    private final String GET_TEAMS_BY_TOURNAMENT_ID = "SELECT * FROM " + TEAMS +
            " WHERE TOURNAMENT_ID=:tournamentId;";

    private final String GET_TEAM_BY_USER_AND_DRAFT_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId AND DRAFT_ID=:draftId;";

    private final String GET_TEAM_BY_USER_AND_GAME_ID = "SELECT * FROM " + TEAMS +
            " WHERE USER_ID=:userId AND GAME_ID=:gameId;";

    private final String UPDATE_TEAM_ROUNDS = "INSERT INTO " + TEAM_ROUNDS +
            " (TEAM_ID, GAME_ID, ROUND_ID, TOURNAMENT_ID, ROUND_NUMBER, TO_PAR," +
            " STROKES, FRONT_NINE, BACK_NINE, SCORES)" +
            " VALUES(:teamId, :gameId, :roundId, :tournamentId, :roundNumber, :toPar," +
            " :strokes, :frontNine, :backNine, :scores)" +
            " ON CONFLICT (TEAM_ID, GAME_ID, ROUND_ID) DO UPDATE SET" +
            " TO_PAR=:toPar, STROKES=:strokes, FRONT_NINE=:frontNine, BACK_NINE=:backNine," +
            " SCORES=:scores;";

    private final String GET_TEAM_ROUNDS_BY_GAME_ID = "SELECT * FROM " + TEAM_ROUNDS +
            " WHERE GAME_ID=:gameId;";

    private final String GET_TEAM_ROUNDS_BY_TEAM_ID = "SELECT * FROM " + TEAM_ROUNDS +
            " WHERE TEAM_ID=:teamId;";

    public TeamDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, TeamRowMapper teamRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamRowMapper = teamRowMapper;
    }

    public void saveTeam(Team team) {
        MapSqlParameterSource params = ParamHelper.getNewTeamParams(team);
        jdbcTemplate.update(SAVE_NEW_TEAM, params);
    }

    public void updateTeam(Team team) {
        MapSqlParameterSource params = ParamHelper.getUpdateTeamParams(team);
        jdbcTemplate.update(UPDATE_TEAM, params);
    }

    public void updateTeams(List<Team> teams) {
        MapSqlParameterSource[] params = ParamHelper.getUpdateTeamsParams(teams);
        jdbcTemplate.batchUpdate(UPDATE_TEAM, params);
    }

    public void deleteTeamsByGameId(UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);

        jdbcTemplate.update(DELETE_TEAMS_BY_GAME_ID, params);
    }

    public void deleteTeamRoundsByGameId(UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);

        jdbcTemplate.update(DELETE_TEAM_ROUNDS_BY_GAME_ID, params);
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

    public List<Team> getTeamsByTournamentId(UUID tournamentId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tournamentId", tournamentId);

        return jdbcTemplate.query(GET_TEAMS_BY_TOURNAMENT_ID, params, teamRowMapper);
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

    public void updateTeamRounds(List<TeamRound> teamRounds) {
        MapSqlParameterSource[] params = ParamHelper.getTeamRoundParams(teamRounds);

        jdbcTemplate.batchUpdate(UPDATE_TEAM_ROUNDS, params);
    }

    public List<TeamRound> getTeamRoundsByGameId(UUID gameId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("gameId", gameId);

        return jdbcTemplate.query(GET_TEAM_ROUNDS_BY_GAME_ID, params, new TeamRoundMapper());
    }

    public List<TeamRound> getTeamRoundsByTeamId(UUID teamId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("teamId", teamId);

        return jdbcTemplate.query(GET_TEAM_ROUNDS_BY_TEAM_ID, params, new TeamRoundMapper());
    }


}
