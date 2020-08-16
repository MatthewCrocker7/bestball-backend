package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.enums.game.GameState;
import crocker.golf.bestball.domain.game.Draft;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.GameDto;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.participant.UserParticipant;
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
    private UserRepository userRepository;
    private UserMapper userMapper;

    private BigDecimal feeMultiplier;

    public GameService(GameValidator gameValidator, UserRepository userRepository, UserMapper userMapper) {
        this.gameValidator = gameValidator;
        this.userRepository = userRepository;
        this.userMapper = userMapper;

        feeMultiplier = new BigDecimal(0.01);
    }

    public void newGame(GameDto gameDto) {

        gameValidator.validateNewGame(gameDto);

        Game game = Game.builder()
                .gameId(UUID.randomUUID())
                .gameState(GameState.NOT_STARTED)
                .draft(getNewDraft(gameDto))
                .tournament(getTournament(gameDto))
                .teams(getNewTeam(gameDto))
                .moneyPot(calculatePot(gameDto))
                .build();

        logger.info("New game created {}", game.getGameId());

        // save game
    }

    private Draft getNewDraft(GameDto gameDto) {
        return Draft.builder()
                .startTime(LocalDateTime.of(gameDto.getDraftDate(), gameDto.getDraftTime()))
                .draftState(DraftState.NOT_STARTED)
                .build();
    }

    private Tournament getTournament(GameDto gameDto) {
        // update this to pass tournament ID. then grab the tournament object with the ID and return here
        return gameDto.getTournament();
    }

    private List<Team> getNewTeam(GameDto gameDto) {
        UserCredentials userCredentials = userRepository.findByEmail(gameDto.getEmail());

        UserParticipant userParticipant = userMapper.convertUserCredentialsToParticipant(userCredentials);

        Team team = Team.builder()
                .userParticipant(userParticipant)
                .playerParticipants(Collections.emptyList())
                .build();

        List<Team> list = new ArrayList<>();
        list.add(team);

        return list;
    }

    private BigDecimal calculatePot(GameDto gameDto) {
        BigDecimal numPlayers = gameDto.getNumPlayers();
        BigDecimal buyIn = gameDto.getBuyIn();

        BigDecimal totalCash = buyIn.multiply(numPlayers);
        BigDecimal fees = totalCash.multiply(feeMultiplier);

        // save fees?

        return totalCash.subtract(fees);
    }


}
