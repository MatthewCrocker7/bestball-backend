package crocker.golf.bestball.core.rest.sports.data;

import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.core.util.TimeHelper;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.sports.data.SportsDataPgaPlayerDto;
import crocker.golf.bestball.domain.pga.Tournament;
import crocker.golf.bestball.domain.pga.sports.data.SportsDataTournamentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.Year;
import java.time.ZoneId;
import java.util.List;

public class SportsDataService implements SportsApiService {

    private static final Logger logger = LoggerFactory.getLogger(SportsDataService.class);

    private RestTemplate restTemplate;
    private SportsDataResponseHelper responseHelper;

    private String apiKey;

    private final String BASE_URL = "https://api.sportsdata.io/golf/v2/json";

    private final String RANKINGS_URL = "/PlayerSeasonStats/{0}"; // year
    private final String SCHEDULE_URL = "/Tournaments/{0}"; // year
    private final String LEADERBOARD_URL = "/Leaderboard/{0}"; // tournament_id
    private final String PLAYER_URL = "/PlayerTournamentStatsByPlayer/{0}/{1}"; // tournament_id,player_id

    public SportsDataService(RestTemplate restTemplate, SportsDataResponseHelper responseHelper, String apiKey) {
        this.restTemplate = restTemplate;
        this.responseHelper = responseHelper;
        this.apiKey = "?key=" + apiKey;
    }

    public List<PgaPlayer> getWorldRankings() throws ExternalAPIException {
        String url = buildRankingsUrl();

        ResponseEntity<List<SportsDataPgaPlayerDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<SportsDataPgaPlayerDto>>() {});

        if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
            throw new ExternalAPIException("Sports API is having internal server issues");
        }

        List<SportsDataPgaPlayerDto> list = responseEntity.getBody();

        return responseHelper.mapResponseToRankings(list);
    }

    public List<Tournament> getSeasonSchedule() throws ExternalAPIException {
        String url = buildScheduleUrl();

        ResponseEntity<List<SportsDataTournamentDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<SportsDataTournamentDto>>() {});

        if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
            throw new ExternalAPIException("Sports API is having internal server issues");
        }

        List<SportsDataTournamentDto> list = responseEntity.getBody();

        return responseHelper.mapResponseToTournaments(list);
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
