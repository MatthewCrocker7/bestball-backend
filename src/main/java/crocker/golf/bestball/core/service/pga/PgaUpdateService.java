package crocker.golf.bestball.core.service.pga;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.domain.enums.pga.Status;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentRound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
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

    public void updateTournamentDetails() {
        List<Tournament> allTournaments = pgaRepository.getAllTournaments();
        //TODO: Ideally we'd update the details of all tournaments that are not complete to accomdate any draft time. For now we only update the current upcoming tourney.
        List<Tournament> tournaments = allTournaments.stream()
                .filter(tournament -> tournament.getTournamentState() != TournamentState.COMPLETE)
                .sorted(Comparator.comparing(Tournament::getStartDate))
                .collect(Collectors.toList()).subList(0, 5);

        tournaments.forEach(tournament -> {
            try {
                Future<Tournament> futureTournamentDetails = sportsApiService.getLatestTournamentDetails(tournament);
                Tournament tournamentDetails = futureTournamentDetails.get();
                pgaRepository.updateTournamentDetails(tournamentDetails);
                logger.info("Tournament details updated for {}", tournamentDetails.getName());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void updateTournamentRound() {
        List<Tournament> tournaments = pgaRepository.getInProgressTournaments();

        tournaments.forEach(tournament -> {

            tournament.setTournamentCourses(pgaRepository.getTournamentCourses(tournament.getTournamentId()));
            tournament.setTournamentRounds(pgaRepository.getTournamentRounds(tournament.getTournamentId()));

            tournament.getTournamentRounds().forEach(tournamentRound -> {
                try {
                    if (tournamentRound.getRoundStatus() == Status.IN_PROGRESS) {
                        logger.info("Updating round {} for tournament {}", tournamentRound.getRoundNumber(), tournament.getName());
                        Future<TournamentRound> updatedRoundFuture = sportsApiService.updateTournamentRound(tournament, tournamentRound);
                        TournamentRound updatedRound = updatedRoundFuture.get();
                        pgaRepository.updatePlayerRounds(updatedRound.getPlayerRounds());
                        logger.info("Player scores updated for round {} of the {}", tournamentRound.getRoundNumber(), tournament.getName());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            });
        });

    }
}
