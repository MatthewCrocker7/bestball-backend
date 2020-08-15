package crocker.golf.bestball.domain.pga;

import crocker.golf.bestball.domain.enums.TournamentState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class Tournament {

    private Long sportsDataTournamentId;
    private UUID sportsRadarTournamentId;
    private Integer version;
    private TournamentState tournamentState;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
