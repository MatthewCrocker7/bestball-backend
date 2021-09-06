package crocker.golf.bestball.core.service.pga;

import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.util.TimeHelper;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        //TODO: Need to make call for both 2020 and 2021 as the season ends in Sept
        List<Tournament> tournaments = pgaRepository.getTournamentsBySeason(TimeHelper.getCurrentSeason());

        return tournaments.stream()
                .filter(tournament -> tournament.getTournamentState() == TournamentState.NOT_STARTED)
                .sorted(Comparator.comparing(Tournament::getStartDate))
                .collect(Collectors.toList());
    }
}
