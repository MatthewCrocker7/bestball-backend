package crocker.golf.bestball.config;

import crocker.golf.bestball.core.dao.*;
import crocker.golf.bestball.core.dao.postgresql.GameDaoImpl;
import crocker.golf.bestball.core.draft.DraftManager;
import crocker.golf.bestball.core.mapper.user.UserMapper;
import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.core.scheduler.GameUpdateScheduler;
import crocker.golf.bestball.core.scheduler.PgaUpdateScheduler;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.core.service.game.*;
import crocker.golf.bestball.core.service.pga.PgaInfoService;
import crocker.golf.bestball.core.service.pga.PgaUpdateService;
import crocker.golf.bestball.core.service.user.EmailService;
import crocker.golf.bestball.core.service.user.UserRegistrationValidator;
import crocker.golf.bestball.core.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import({DatabaseConfig.class, RestConfig.class, DraftConfig.class, WebSocketConfig.class})
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
    public EmailService emailService(JavaMailSender javaMailSender) {
        return new EmailService(javaMailSender);
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public GameCreatorService gameService(GameValidator gameValidator, GameRepository gameRepository, UserRepository userRepository, PgaRepository pgaRepository, DraftManager draftManager) {
        return new GameCreatorService(gameValidator, gameRepository, userRepository, pgaRepository, draftManager);
    }

    @Bean
    public GameManagerService gameManagerService(GameRepository gameRepository, UserRepository userRepository, PgaRepository pgaRepository) {
        return new GameManagerService(gameRepository, userRepository, pgaRepository);
    }

    @Bean
    public GameValidator gameValidator() {
        return new GameValidator();
    }

    @Bean
    public GameUpdateScheduler gameUpdateScheduler(GameManagerService gameManagerService) {
        return new GameUpdateScheduler(gameManagerService);
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

    @Bean
    public DraftService draftService(DraftRepository draftRepository, GameRepository gameRepository, UserRepository userRepository, PgaRepository pgaRepository) {
        return new DraftService(draftRepository, userRepository, gameRepository, pgaRepository);
    }

    @Bean
    public InfoService infoService(DraftRepository draftRepository, GameRepository gameRepository, UserRepository userRepository) {
        return new InfoService(draftRepository, gameRepository, userRepository);
    }

    @Bean
    public UserRepository userRepository(UserDao userDao) {
        return new UserRepository(userDao);
    }

    @Bean
    public PgaRepository pgaRepository(PgaDao pgaDao) {
        return new PgaRepository(pgaDao);
    }

    @Bean
    public GameRepository gameRepository(GameDaoImpl gameDaoImpl, TeamDao teamDao) { return new GameRepository(gameDaoImpl, teamDao); }

    @Bean
    public DraftRepository draftRepository(DraftDao draftDao) { return new DraftRepository(draftDao); }

}
