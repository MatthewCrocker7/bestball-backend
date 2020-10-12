package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.game.GameCreatorService;
import crocker.golf.bestball.core.service.game.GameManagerService;
import crocker.golf.bestball.domain.exceptions.game.TeamNotAuthorizedException;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.GameDto;
import crocker.golf.bestball.domain.user.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private GameCreatorService gameCreatorService;
    private GameManagerService gameManagerService;

    public GameController(GameCreatorService gameCreatorService, GameManagerService gameManagerService) {
        this.gameCreatorService = gameCreatorService;
        this.gameManagerService = gameManagerService;
    }

    @PostMapping("/newGame")
    public ResponseEntity newGame(@RequestBody GameDto gameDto) {
        try {
            logger.info("Received request to create new game");

            gameCreatorService.newGame(gameDto);

            return new ResponseEntity(null, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/joinGame")
    public ResponseEntity joinGame(@RequestBody GameDto gameDto) {
        gameCreatorService.joinGame(gameDto);

        return new ResponseEntity(null, null, HttpStatus.OK);
    }

    @PostMapping("/addToDraft")
    public ResponseEntity addToDraft(@RequestBody RequestDto requestDto) {
        try {
            logger.info("Received request from {} to add user {} to draft {}", requestDto.getEmail(), requestDto.getInviteEmail(), requestDto.getDraftId());
            gameCreatorService.addToDraft(requestDto);

            return new ResponseEntity(null, null, HttpStatus.OK);
        } catch (TeamNotAuthorizedException e) {
            return new ResponseEntity<>(e, null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/addToGame")
    public ResponseEntity addToGame(@RequestBody RequestDto requestDto) {
        try {
            gameCreatorService.addToGame(requestDto);

            return new ResponseEntity(null, null, HttpStatus.OK);
        } catch (TeamNotAuthorizedException e) {
            return new ResponseEntity<>(e, null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/loadGame")
    public ResponseEntity getUpcomingTournaments(@RequestBody RequestDto requestDto) {
        logger.info("Received request from {} to load game {}", requestDto.getEmail(), requestDto.getGameId());
        Game game = gameManagerService.loadGame(requestDto);

        return new ResponseEntity<>(game, null, HttpStatus.OK);
    }
}
