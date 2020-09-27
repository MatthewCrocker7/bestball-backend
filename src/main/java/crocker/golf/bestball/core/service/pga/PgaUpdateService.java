package crocker.golf.bestball.core.service.pga;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.domain.enums.pga.Status;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentRound;
import crocker.golf.bestball.domain.pga.tournament.TournamentSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PgaUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateService.class);

    private SportsApiService sportsApiService;
    private PgaRepository pgaRepository;

    public PgaUpdateService(SportsApiService sportsApiService, PgaRepository pgaRepository) {
        this.sportsApiService = sportsApiService;
        this.pgaRepository = pgaRepository;
    }

    // update individual player scores with /PlayerTournamentStatsByPlayer
    // update leaderboard with /Leaderboard

    public void updateWorldRankings() throws Exception {

        Future<List<PgaPlayer>> futurePgaPlayers = sportsApiService.getWorldRankings();
        List<PgaPlayer> pgaPlayers = futurePgaPlayers.get();

        pgaRepository.updateWorldRankings(pgaPlayers);

        logger.info("World golf rankings updated with top {} players", pgaPlayers.size());
    }

    public void updateSeasonSchedule() throws Exception {

        Future<List<Tournament>> futureTournaments = sportsApiService.getSeasonSchedule();
        List<Tournament> tournaments = futureTournaments.get();

        pgaRepository.updateSeasonSchedule(tournaments);
    }

    public void updateTournamentSummary() {
        List<Tournament> tournaments = pgaRepository.getAllTournaments();
        List<Tournament> updateTournaments = tournaments.stream()
                .filter(tournament -> tournament.getTournamentState() == TournamentState.IN_PROGRESS)
                .collect(Collectors.toList());

        updateTournaments.forEach(tournament -> {
            try {
                Future<TournamentSummary> test = sportsApiService.updateTournamentSummary(tournament);
                TournamentSummary tournamentSummary = test.get();
                pgaRepository.updateTournamentSummary(tournamentSummary);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        });
    }

    public void updateTournamentRound() {

        List<Tournament> tournaments = pgaRepository.getAllTournaments();
        List<Tournament> updateTournaments = tournaments.stream()
                .filter(tournament -> tournament.getTournamentState() == TournamentState.IN_PROGRESS)
                .collect(Collectors.toList());

        updateTournaments.forEach(tournament -> {
            TournamentSummary tournamentSummary = TournamentSummary.builder()
                    .tournamentId(tournament.getTournamentId())
                    .name(tournament.getName())
                    .season(tournament.getSeason())
                    .tournamentCourses(pgaRepository.getTournamentCourses(tournament.getTournamentId()))
                    .tournamentRounds(pgaRepository.getTournamentRounds(tournament.getTournamentId()))
                    .tournamentStatus(null)
                    .build();

            tournamentSummary.getTournamentRounds().forEach(tournamentRound -> {
                try {
                    if (tournamentRound.getRoundStatus() != Status.CLOSED) {
                        logger.info("Updating round {} for tournament {}", tournamentRound.getRoundNumber(), tournamentSummary.getName());
                        Future<TournamentRound> test = sportsApiService.updateTournamentRound(tournamentSummary, tournamentRound);
                        TournamentRound round = test.get();
                        logger.info("Persist player round scores");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            });
        });

    }
}
