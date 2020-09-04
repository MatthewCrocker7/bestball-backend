package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.TeamInfo;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;

import java.util.List;
import java.util.stream.Collectors;

public class InfoService {

    private DraftRepository draftRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;

    public InfoService(DraftRepository draftRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.draftRepository = draftRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public List<TeamInfo> getTeamInfo(UserCredentialsDto userCredentialsDto) {
        String email = userCredentialsDto.getEmail();
        UserCredentials userCredentials = userRepository.findByEmail(email);

        List<Team> teams = gameRepository.getTeamsByUserId(userCredentials.getUserId());

        return enrichTeamInfo(teams);
    }

    private List<TeamInfo> enrichTeamInfo(List<Team> teams) {
        //TODO: This will likely need to be optimized with join query
        return teams.stream().map(team -> TeamInfo.builder()
            .teamId(team.getTeamId())
            .draft(draftRepository.getLatestDraftById(team.getDraftId()))
            .game(gameRepository.getLatestGameByGameId(team.getGameId()))
            .build()
        ).collect(Collectors.toList());
    }
}
