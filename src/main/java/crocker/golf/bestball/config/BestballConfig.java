package crocker.golf.bestball.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import crocker.golf.bestball.core.mapper.UserMapper;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsDataService;
import crocker.golf.bestball.core.scheduler.PgaUpdateScheduler;
import crocker.golf.bestball.core.repository.UserRepository;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

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
    public PgaUpdateService pgaUpdateService(SportsDataService sportsDataService, PgaRepository pgaRepository) {
        return new PgaUpdateService(sportsDataService, pgaRepository);
    }

    @Bean
    public SportsDataService sportsDataService(RestTemplate restTemplate, @Value("${golf.api.key}") String apiKey) {
        ObjectMapper mapper = new ObjectMapper();
        return new SportsDataService(restTemplate, mapper, apiKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
