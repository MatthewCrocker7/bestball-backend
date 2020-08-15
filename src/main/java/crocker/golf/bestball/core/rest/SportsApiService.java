package crocker.golf.bestball.core.rest;

import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;

import java.util.List;

public interface SportsApiService {

    List<PgaPlayer> getWorldRankings() throws ExternalAPIException;

    List<Tournament> getSeasonSchedule() throws ExternalAPIException;


}
