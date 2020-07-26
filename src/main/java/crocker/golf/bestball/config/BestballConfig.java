package crocker.golf.bestball.config;

import crocker.golf.bestball.core.controllers.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.yaml"}, ignoreResourceNotFound = true)
public class BestballConfig {
    private static final Logger logger = LoggerFactory.getLogger(BestballConfig.class);

    @Value("${server.env}")
    private String environment;

    @Bean
    public UserController userController() {
        logger.info("Loading environment {}", environment);

        return new UserController();
    }
}
