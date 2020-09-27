package crocker.golf.bestball.domain.pga.tournament;

import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
public class Tournament {

    private UUID tournamentId;
    private EventType eventType;
    private Integer season;
    private TournamentState tournamentState;
    private String name;
    private LocalDateTime startDate;
    private LocalDate endDate;

}
