package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;

import java.util.Collections;
import java.util.List;

public class DraftService {

    private DraftRepository draftRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;

    public DraftService(DraftRepository draftRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.draftRepository = draftRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public List<Draft> getDrafts(UserCredentialsDto userCredentialsDto) {
        String email = userCredentialsDto.getEmail();
        UserCredentials userCredentials = userRepository.findByEmail(email);



        return Collections.emptyList();
    }
}
