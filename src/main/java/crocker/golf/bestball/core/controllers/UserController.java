package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.UserService;
import crocker.golf.bestball.domain.UserCredentialsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserCredentialsDto userCredentialsDto) {
        logger.info("Received request to register new user {}", userCredentialsDto.getUsername());
        userService.register(userCredentialsDto);
    }

}
