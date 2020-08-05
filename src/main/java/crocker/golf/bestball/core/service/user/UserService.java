package crocker.golf.bestball.core.service.user;

import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.exceptions.user.PasswordNotMatchException;
import crocker.golf.bestball.domain.exceptions.user.UserNotExistException;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.exceptions.user.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserService implements UserDetailsService, UserDetailsPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private UserRegistrationValidator userRegistrationValidator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserRegistrationValidator userRegistrationValidator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRegistrationValidator = userRegistrationValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredentials userCredentials = userRepository.findByEmail(email);

        return userMapper.convertUserToUserDetails(userCredentials);
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {

        UserCredentials userCredentials = userRepository.findByUsername(userDetails.getUsername());
        userCredentials.setPassword(newPassword);

        return userMapper.convertUserToUserDetails(userCredentials);
    }

    public void register(UserCredentialsDto userCredentialsDto) throws RegistrationException {
        userRegistrationValidator.validateNewUser(userCredentialsDto);

        UserCredentials userCredentials = UserCredentials.builder()
                .userId(UUID.randomUUID())
                .enabled(false)
                .userName(userCredentialsDto.getUserName())
                .email(userCredentialsDto.getEmail())
                .firstName(userCredentialsDto.getFirstName())
                .lastName(userCredentialsDto.getLastName())
                .password(passwordEncoder.encode(userCredentialsDto.getPassword()))
                .build();

        userRepository.save(userCredentials);
        logger.info("UserCredentials registered successfully.");
    }

    public void login(UserCredentialsDto userCredentialsDto) throws UserNotExistException, PasswordNotMatchException {
        String email = userCredentialsDto.getEmail();
        String loginPassword = userCredentialsDto.getPassword();

        UserCredentials userCredentials = userRepository.findByEmail(email);

        if(userCredentials == null) {
            throw new UserNotExistException("An account with the given email does not exist.");
        }

        if(passwordEncoder.matches(loginPassword, userCredentials.getPassword())) {
            logger.info("Authentication success for {}", userCredentials.getUserName());
        } else {
            throw new PasswordNotMatchException("Incorrect password");
        }
    }
}
