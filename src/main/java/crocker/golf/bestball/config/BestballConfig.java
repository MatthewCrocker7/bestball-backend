package crocker.golf.bestball.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.yaml"}, ignoreResourceNotFound = true)
public class BestballConfig {
}
