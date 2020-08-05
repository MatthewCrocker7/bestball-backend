package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.user.UserService;
import crocker.golf.bestball.domain.exceptions.user.PasswordNotMatchException;
import crocker.golf.bestball.domain.exceptions.user.UserNotExistException;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.exceptions.user.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            logger.info("Received request to login user");
            userService.login(userCredentialsDto);

            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (UserNotExistException e) {
            logger.error("Failed to login because user does not exist");
            return new ResponseEntity<>(e, null, HttpStatus.NOT_FOUND);
        } catch (PasswordNotMatchException e) {
            logger.error("Failed to login because password was incorrect");
            return new ResponseEntity<>(e, null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // User submits this request from login page. Backend should return an object
        // { authenticated: true, expireTime: someTime, userDto} and save clientContext in cache?
        // Additional requests from main frontend would than be authenticated by comparing userCredentials with clientContext on backend
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        logger.info("Received request to logout user.");
        // do I need to clear a client context here?
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }


}
