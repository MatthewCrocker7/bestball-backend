package crocker.golf.bestball.core.rest;

import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

public interface SportsApiService {

    @Async
    Future<List<PgaPlayer>> getWorldRankings() throws ExternalAPIException;

    @Async
    Future<List<Tournament>> getSeasonSchedule() throws ExternalAPIException;


}
