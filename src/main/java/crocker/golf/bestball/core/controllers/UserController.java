package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.user.UserService;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.exceptions.user.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            logger.info("Received request to register new user");
            userService.register(userCredentialsDto);

            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (RegistrationException e) {
            logger.error("Error {}", e.toString());
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Failed to register user because of server error.");
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
