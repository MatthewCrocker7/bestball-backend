package crocker.golf.bestball.domain.pga.tournament;

import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.Status;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Tournament {

    private UUID tournamentId;
    private String name;
    private TournamentState tournamentState;
    private Integer season;

    private EventType eventType;
    private LocalDateTime startDate;
    private LocalDate endDate;
    private Status tournamentStatus;

    private List<PgaPlayer> tournamentField;
    private List<TournamentCourse> tournamentCourses;
    private List<TournamentRound> tournamentRounds;

}
