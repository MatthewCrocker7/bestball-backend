package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentials;

import java.util.List;
import java.util.UUID;

public class GameManagerService {

    private GameRepository gameRepository;
    private UserRepository userRepository;
    private PgaRepository pgaRepository;

    public GameManagerService(GameRepository gameRepository, UserRepository userRepository, PgaRepository pgaRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.pgaRepository = pgaRepository;
    }

    public Game loadGame(RequestDto requestDto) {
        UUID gameId = UUID.fromString(requestDto.getGameId());
        Game game = gameRepository.getLatestGameByGameId(gameId);
        UserCredentials userCredentials = userRepository.findByEmail(requestDto.getEmail());

        return enrichGame(game, userCredentials);
    }

    private Game enrichGame(Game game, UserCredentials userCredentials) {
        Game enrichedGame = Game.builder()
                .gameId(game.getGameId())
                .gameState(game.getGameState())
                .gameVersion(game.getGameVersion())
                .gameType(game.getGameType())
                .draftId(game.getDraftId())
                .tournament(game.getTournament())
                .numPlayers(game.getNumPlayers())
                .buyIn(game.getBuyIn())
                .moneyPot(game.getMoneyPot())
                .build();

        enrichTeams(enrichedGame);

        return enrichedGame;
    }

    private void enrichTeams(Game game) {
        List<Team> teams = gameRepository.getTeamsByDraftId(game.getDraftId());





        game.setTeams(teams);
    }
}
