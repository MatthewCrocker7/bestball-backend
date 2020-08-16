package crocker.golf.bestball.core.rest.sports.radar;

import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.core.util.TimeHelper;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import crocker.golf.bestball.domain.pga.sports.data.SportsDataPgaPlayerDto;
import crocker.golf.bestball.domain.pga.sports.radar.SportsRadarScheduleDto;
import crocker.golf.bestball.domain.pga.sports.radar.SportsRadarWorldGolfRankingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class SportsRadarService implements SportsApiService {

    private static final Logger logger = LoggerFactory.getLogger(SportsRadarService.class);

    private RestTemplate restTemplate;
    private SportsRadarResponseHelper sportsRadarResponseHelper;

    private String apiKey;

    private final String BASE_URL = "http://api.sportradar.us/golf-t2";

    private final String RANKINGS_URL = "/players/wgr/{0}/rankings.json";
    private final String SCHEDULE_URL = "/schedule/pga/{0}/tournaments/schedule.json";

    public SportsRadarService(RestTemplate restTemplate, SportsRadarResponseHelper sportsRadarResponseHelper, String apiKey) {
        this.restTemplate = restTemplate;
        this.sportsRadarResponseHelper = sportsRadarResponseHelper;
        this.apiKey = "?api_key=" + apiKey;
    }

    public List<PgaPlayer> getWorldRankings() throws ExternalAPIException {
        String url = buildRankingsUrl();

        ResponseEntity<SportsRadarWorldGolfRankingDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, SportsRadarWorldGolfRankingDto.class);

        if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
            throw new ExternalAPIException("Sports API is having internal server issues");
        }

        SportsRadarWorldGolfRankingDto sportsRadarWorldGolfRankingDto = responseEntity.getBody();

        return sportsRadarResponseHelper.mapResponseToRankings(sportsRadarWorldGolfRankingDto);
    }

    public List<Tournament> getSeasonSchedule() throws ExternalAPIException {
        String url = buildScheduleUrl();

        ResponseEntity<SportsRadarScheduleDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, SportsRadarScheduleDto.class);

        if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
            throw new ExternalAPIException("Sports API is having internal server issues");
        }

        SportsRadarScheduleDto scheduleDto = responseEntity.getBody();

        return sportsRadarResponseHelper.mapResponseToTournaments(scheduleDto);
    }

    private String buildRankingsUrl() {
        int year = TimeHelper.getCurrentSeason();
        return BASE_URL + MessageFormat.format(RANKINGS_URL, Integer.toString(year)) + apiKey;
    }

    private String buildScheduleUrl() {
        int year = TimeHelper.getCurrentSeason();
        return BASE_URL + MessageFormat.format(SCHEDULE_URL, Integer.toString(year)) + apiKey;
    }
}
