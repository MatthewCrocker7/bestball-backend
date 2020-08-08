package crocker.golf.bestball.core.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.PgaPlayerDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SportsDataService {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private String apiKey;

    private final String BASE_URL = "https://api.sportsdata.io/golf/v2/json";

    private final String RANKINGS_URL = "/PlayerSeasonStats/{0}"; // year
    private final String SCHEDULE_URL = "/Tournaments/{0}"; // year
    private final String LEADERBOARD_URL = "/Leaderboard/{0}"; // tournament_id
    private final String PLAYER_URL = "/PlayerTournamentStatsByPlayer/{0}/{1}"; // tournament_id,player_id

    public SportsDataService(RestTemplate restTemplate, ObjectMapper objectMapper, String apiKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public List<PgaPlayer> getWorldRankings() throws ExternalAPIException {
        String url = buildRankingsUrl(2020);

        ResponseEntity<List<PgaPlayerDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PgaPlayerDto>>() {});

        if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
            throw new ExternalAPIException("Sports API is having internal server issues");
        }

        List<PgaPlayerDto> list = responseEntity.getBody();

        return ResponseHelper.mapResponseToRankings(list);
    }

    private String buildRankingsUrl(int year) {
        return BASE_URL + MessageFormat.format(RANKINGS_URL, Integer.toString(year)) + addApiKey();
    }

    private String addApiKey() {
        return "?key=" + apiKey;
    }

    public HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Ocp-Apim-Subscription-Key:", apiKey);
        return headers;
    }
}
