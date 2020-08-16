package crocker.golf.bestball.core.service.pga;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PgaInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PgaUpdateService.class);

    private PgaRepository pgaRepository;

    public PgaInfoService(PgaRepository pgaRepository) {
        this.pgaRepository = pgaRepository;
    }

    public List<Tournament> getUpcomingTournaments() {
        List<Tournament> tournaments = pgaRepository.getCurrentSeasonTournaments();

        return tournaments.stream()
                .filter(tournament -> tournament.getTournamentState() == TournamentState.NOT_STARTED)
                .sorted(Comparator.comparing(Tournament::getStartDate))
                .collect(Collectors.toList());
    }
}
