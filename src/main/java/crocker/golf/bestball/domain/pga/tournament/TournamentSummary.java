package crocker.golf.bestball.domain.pga.tournament;

import crocker.golf.bestball.domain.enums.pga.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class TournamentSummary {

    private UUID tournamentId;
    private String name;
    private Status tournamentStatus;
    private Integer season;

    private List<TournamentCourse> tournamentCourses;
    private List<TournamentRound> tournamentRounds;
}
