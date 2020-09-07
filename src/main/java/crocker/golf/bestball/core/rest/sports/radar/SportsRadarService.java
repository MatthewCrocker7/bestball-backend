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
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SportsRadarService implements SportsApiService {

    private static final Logger logger = LoggerFactory.getLogger(SportsRadarService.class);

    //TODO: On api key forbidden failure, cycle to next key in list and retry
    private RestTemplate restTemplate;
    private SportsRadarResponseHelper sportsRadarResponseHelper;

    private final LinkedList<String> keys;

    private final String BASE_URL = "http://api.sportradar.us/golf-t2";

    private final String RANKINGS_URL = "/players/wgr/{0}/rankings.json";
    private final String SCHEDULE_URL = "/schedule/pga/{0}/tournaments/schedule.json";

    public SportsRadarService(RestTemplate restTemplate, SportsRadarResponseHelper sportsRadarResponseHelper, String keys) {
        this.restTemplate = restTemplate;
        this.sportsRadarResponseHelper = sportsRadarResponseHelper;
        // this.apiKey = "?api_key=" + apiKey;
        this.keys = Stream.of(keys.split(","))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Retryable(
        value = { HttpClientErrorException.class },
        maxAttempts = 100,
        backoff = @Backoff(2000)
    )
    @Async
    public Future<List<PgaPlayer>> getWorldRankings() throws ExternalAPIException {
        String url = buildRankingsUrl();
        logger.info("Attempting to retrieve world rankings on thread {}", Thread.currentThread().getName());
        try {
            ResponseEntity<SportsRadarWorldGolfRankingDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, SportsRadarWorldGolfRankingDto.class);

            if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
                throw new ExternalAPIException("Sports API is having internal server issues");
            }

            SportsRadarWorldGolfRankingDto sportsRadarWorldGolfRankingDto = responseEntity.getBody();

            List<PgaPlayer> pgaPlayers = sportsRadarResponseHelper.mapResponseToRankings(sportsRadarWorldGolfRankingDto);

            return new AsyncResult<>(pgaPlayers);
        } catch (HttpClientErrorException e) {
            shiftKeys();
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
    }

    @Retryable(
            value = { HttpClientErrorException.class },
            maxAttempts = 100,
            backoff = @Backoff(2000)
    )
    @Async
    public Future<List<Tournament>> getSeasonSchedule() throws ExternalAPIException {
        String url = buildScheduleUrl();
        logger.info("Attempting to retrieve season schedule on thread {}", Thread.currentThread().getName());
        try {
            ResponseEntity<SportsRadarScheduleDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, SportsRadarScheduleDto.class);

            if(responseEntity.getStatusCode()!= HttpStatus.OK || responseEntity.getBody() == null) {
                throw new ExternalAPIException("Sports API is having internal server issues");
            }

            SportsRadarScheduleDto scheduleDto = responseEntity.getBody();

            List<Tournament> tournaments = sportsRadarResponseHelper.mapResponseToTournaments(scheduleDto);

            return new AsyncResult<>(tournaments);

        } catch (HttpClientErrorException e) {
            shiftKeys();
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }

    }

    private String buildRankingsUrl() {
        int year = TimeHelper.getCurrentSeason();
        return BASE_URL + MessageFormat.format(RANKINGS_URL, Integer.toString(year)) + addKey();
    }

    private String buildScheduleUrl() {
        //TODO: Need to make call for both 2020 and 2021 as the season ends in Sept
        int year = TimeHelper.getCurrentSeason();
        return BASE_URL + MessageFormat.format(SCHEDULE_URL, Integer.toString(year + 1)) + addKey();
    }

    private void shiftKeys() {
        logger.error("Api key expired. Shifting keys");
        String key = keys.removeFirst();
        keys.addLast(key);
    }

    private String addKey() {
        return "?api_key=" + keys.getFirst();
    }
}
