package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.enums.game.GameState;
import crocker.golf.bestball.domain.enums.game.GameType;
import crocker.golf.bestball.domain.enums.game.TeamRole;
import crocker.golf.bestball.domain.game.Draft;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.GameDto;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.round.TeamRound;
import crocker.golf.bestball.domain.pga.Tournament;
import crocker.golf.bestball.domain.user.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private GameValidator gameValidator;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PgaRepository pgaRepository;

    private BigDecimal feeMultiplier;

    public GameService(GameValidator gameValidator, GameRepository gameRepository, UserRepository userRepository, UserMapper userMapper, PgaRepository pgaRepository) {
        this.gameValidator = gameValidator;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pgaRepository = pgaRepository;

        feeMultiplier = new BigDecimal(0.01);
    }

    public void newGame(GameDto gameDto) {

        gameValidator.validateNewGame(gameDto);

        Draft draft = getNewDraft(gameDto);

        Game game = Game.builder()
                .gameId(UUID.randomUUID())
                .gameState(GameState.NOT_STARTED)
                .gameType(getGameType(gameDto))
                .gameVersion(1)
                .draftId(draft.getDraftId())
                .tournament(getTournament(gameDto))
                .numPlayers(gameDto.getNumPlayers())
                .moneyPot(calculatePot(gameDto))
                .build();

        gameRepository.saveNewGame(game);
        gameRepository.saveNewDraft(draft);
        logger.info("New game created {}", game.getGameId());

        makeCreatorTeam(gameDto, game);
        // save user one
    }

    public void joinGame(GameDto gameDto) {
        UserCredentials userCredentials = userRepository.findByEmail(gameDto.getEmail());
        Game game = gameRepository.getGameById(gameDto);

        Team team = Team.builder()
                .teamId(UUID.randomUUID())
                .userId(userCredentials.getUserId())
                .draftId(game.getDraftId())
                .gameId(game.getGameId())
                .teamRole(TeamRole.PARTICIPANT)
                .build();
    }

    private void makeCreatorTeam(GameDto gameDto, Game game) {
        UserCredentials userCredentials = userRepository.findByEmail(gameDto.getEmail());

        Team team = Team.builder()
                .teamId(UUID.randomUUID())
                .userId(userCredentials.getUserId())
                .draftId(game.getDraftId())
                .gameId(game.getGameId())
                .teamRole(TeamRole.CREATOR)
                .build();
    }

    private Draft getNewDraft(GameDto gameDto) {
        return Draft.builder()
                .draftId(UUID.randomUUID())
                .startTime(LocalDateTime.of(gameDto.getDraftDate(), gameDto.getDraftTime()))
                .draftState(DraftState.NOT_STARTED)
                .build();
    }

    private Tournament getTournament(GameDto gameDto) {
        return pgaRepository.getTournamentById(gameDto.getTournamentId());
    }

    private GameType getGameType(GameDto gameDto) {
        return GameType.valueOf(gameDto.getGameType().toUpperCase());
    }

    private BigDecimal calculatePot(GameDto gameDto) {
        Integer numPlayers = gameDto.getNumPlayers();
        BigDecimal buyIn = gameDto.getBuyIn();

        BigDecimal totalCash = buyIn.multiply(BigDecimal.valueOf(numPlayers));
        BigDecimal fees = totalCash.multiply(feeMultiplier);

        // save fees?

        return totalCash.subtract(fees);
    }


}
