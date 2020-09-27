package crocker.golf.bestball.core.rest;

import crocker.golf.bestball.domain.exceptions.ExternalAPIException;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentRound;
import crocker.golf.bestball.domain.pga.tournament.TournamentSummary;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

public interface SportsApiService{

    @Async
    Future<List<PgaPlayer>> getWorldRankings() throws ExternalAPIException;

    @Async
    Future<List<Tournament>> getSeasonSchedule() throws ExternalAPIException;

    @Async
    Future<TournamentSummary> updateTournamentSummary(Tournament tournament) throws ExternalAPIException;

    @Async
    Future<TournamentRound> updateTournamentRound(TournamentSummary tournamentSummary, TournamentRound tournamentRound) throws ExternalAPIException;
}
