package crocker.golf.bestball.config;

import crocker.golf.bestball.core.dao.UserDao;
import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.replay.PgaUpdateScheduler;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.core.service.PgaUpdateService;
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
@Import({DatabaseConfig.class})
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
    public PgaUpdateScheduler pgaUpdateScheduler(PgaUpdateService pgaUpdateService) {
        return new PgaUpdateScheduler(pgaUpdateService);
    }

    @Bean
    public PgaUpdateService pgaUpdateService() {
        return new PgaUpdateService();
    }

}
