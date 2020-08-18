package crocker.golf.bestball.config;

import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.core.scheduler.PgaUpdateScheduler;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.core.service.game.GameService;
import crocker.golf.bestball.core.service.game.GameValidator;
import crocker.golf.bestball.core.service.pga.PgaInfoService;
import crocker.golf.bestball.core.service.pga.PgaUpdateService;
import crocker.golf.bestball.core.service.user.UserRegistrationValidator;
import crocker.golf.bestball.core.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import({DatabaseConfig.class, RestConfig.class})
@PropertySource(value = {"classpath:application.yaml"}, ignoreResourceNotFound = true)
public class BestballConfig {
    private static final Logger logger = LoggerFactory.getLogger(BestballConfig.class);

    @Value("${server.env}")
    private String environment;

    @Bean
    public UserService userService(UserRepository userRepository, UserMapper userMapper,
                                   UserRegistrationValidator userRegistrationValidator, PasswordEncoder passwordEncoder) {
        logger.info("Loading environment {}", environment);
        return new UserService(userRepository, userMapper, userRegistrationValidator, passwordEncoder);
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public UserRegistrationValidator userRegistrationValidator(UserRepository userRepository) {
        return new UserRegistrationValidator(userRepository);
    }

    @Bean
    public GameService gameService(GameValidator gameValidator, GameRepository gameRepository, UserRepository userRepository, UserMapper userMapper, PgaRepository pgaRepository) {
        return new GameService(gameValidator, gameRepository, userRepository, userMapper, pgaRepository);
    }

    @Bean
    public GameValidator gameValidator() {
        return new GameValidator();
    }

    @Bean
    public PgaUpdateScheduler pgaUpdateScheduler(PgaUpdateService pgaUpdateService) {
        return new PgaUpdateScheduler(pgaUpdateService);
    }

    @Bean
    public PgaUpdateService pgaUpdateService(SportsApiService sportsApiService, PgaRepository pgaRepository) {
        return new PgaUpdateService(sportsApiService, pgaRepository);
    }

    @Bean
    public PgaInfoService pgaInfoService(PgaRepository pgaRepository) {
        return new PgaInfoService(pgaRepository);
    }

}
