package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.user.EmailService;
import crocker.golf.bestball.core.service.user.UserService;
import crocker.golf.bestball.domain.events.RegisterNewUserEvent;
import crocker.golf.bestball.domain.exceptions.user.PasswordNotMatchException;
import crocker.golf.bestball.domain.exceptions.user.UserNotExistException;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.exceptions.user.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final EmailService emailService;

    private ApplicationEventPublisher eventPublisher;

    @Value("${user.url.base}")
    private String userUrl;

    public UserController(UserService userService, EmailService emailService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            logger.info("Received request to register new user");
            UserCredentials userCredentials = userService.register(userCredentialsDto);

            eventPublisher.publishEvent(new RegisterNewUserEvent(userCredentials, userUrl));
            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        logger.info("Received request to logout user.");
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    @PostMapping("/inviteToDraft")
    public ResponseEntity inviteToDraft(@RequestBody RequestDto requestDto) {
        logger.info("Received request from {} to invite {} to draft {}", requestDto.getEmail(), requestDto.getInviteEmail(), requestDto.getDraftId());
        emailService.invitePlayerToDraft(requestDto);

        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }


}
