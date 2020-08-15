package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.game.GameService;
import crocker.golf.bestball.domain.game.GameDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/newGame")
    public ResponseEntity newGame(@RequestBody GameDto gameDto) {
        try {
            logger.info("Received request to create new game");

            gameService.newGame(gameDto);

            return new ResponseEntity(null, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
