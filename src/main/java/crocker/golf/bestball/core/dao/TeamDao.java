package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.round.TeamRound;

import java.util.List;
import java.util.UUID;

public interface TeamDao {

    void saveTeam(Team team);

    void updateTeam(Team team);

    void updateTeams(List<Team> teams);

    List<Team> getTeamsByUserId(UUID userId);

    List<Team> getTeamsByDraftId(UUID draftId);

    List<Team> getTeamsByTournamentId(UUID tournamentId);

    Team getTeamByUserAndDraftId(UUID userId, UUID draftId);

    Team getTeamByUserAndGameId(UUID userId, UUID gameId);

    void updateTeamRounds(List<TeamRound> teamRounds);

    List<TeamRound> getTeamRoundsByGameId(UUID gameId);

    List<TeamRound> getTeamRoundsByTeamId(UUID teamId);
}
