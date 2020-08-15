package crocker.golf.bestball.config;

import crocker.golf.bestball.core.rest.sports.data.SportsDataResponseHelper;
import crocker.golf.bestball.core.rest.sports.data.SportsDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource(value = {"classpath:application.yaml"}, ignoreResourceNotFound = true)
public class RestConfig {

    @Bean
    public SportsDataService sportsDataService(RestTemplate restTemplate, SportsDataResponseHelper sportsDataResponseHelper, @Value("${golf.api.key}") String apiKey) {
        return new SportsDataService(restTemplate, sportsDataResponseHelper, apiKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SportsDataResponseHelper sportsDataResponseHelper() {
        return new SportsDataResponseHelper();
    }
}
