package crocker.golf.bestball.core.rest.sports.radar;

import crocker.golf.bestball.core.rest.SportsApiService;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;

import java.util.Collections;
import java.util.List;

public class SportsRadarService implements SportsApiService {

    private SportsRadarResponseHelper sportsRadarResponseHelper;

    public SportsRadarService(SportsRadarResponseHelper sportsRadarResponseHelper) {
        this.sportsRadarResponseHelper = sportsRadarResponseHelper;
    }

    public List<PgaPlayer> getWorldRankings() {
        return Collections.emptyList();
    }

    public List<Tournament> getSeasonSchedule() {
        return Collections.emptyList();
    }
}
