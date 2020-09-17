package crocker.golf.bestball.config;

import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.core.rest.sports.data.SportsDataResponseHelper;
import crocker.golf.bestball.core.rest.sports.radar.SportsRadarResponseHelper;
import crocker.golf.bestball.core.rest.sports.radar.SportsRadarService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource(value = {"classpath:application.yaml"}, ignoreResourceNotFound = true)
public class RestConfig {

    @Bean
    public SportsApiService sportsRadarService(RestTemplate restTemplate, SportsRadarResponseHelper sportsRadarResponseHelper, @Value("${golf.api.key.sports.radar}") String apiKey) {
        return new SportsRadarService(restTemplate, sportsRadarResponseHelper, apiKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SportsDataResponseHelper sportsDataResponseHelper() {
        return new SportsDataResponseHelper();
    }

    @Bean
    public SportsRadarResponseHelper sportsRadarResponseHelper() {
        return new SportsRadarResponseHelper();
    }
}
