package crocker.golf.bestball.config;

import crocker.golf.bestball.core.controllers.UserController;
import crocker.golf.bestball.core.dao.UserDao;
import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.replay.PgaUpdateScheduler;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.core.service.PgaUpdateService;
import crocker.golf.bestball.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import({DatabaseConfig.class})
@PropertySource(value = {"classpath:application.yaml"}, ignoreResourceNotFound = true)
public class BestballConfig {
    private static final Logger logger = LoggerFactory.getLogger(BestballConfig.class);

    @Value("${server.env}")
    private String environment;

    @Bean
    public UserService userService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        logger.info("Loading environment {}", environment);
        return new UserService(userRepository, userMapper, passwordEncoder);
    }

    @Bean
    public UserRepository userRepository(UserDao userDao) {
        return new UserRepository(userDao);
    }

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
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
