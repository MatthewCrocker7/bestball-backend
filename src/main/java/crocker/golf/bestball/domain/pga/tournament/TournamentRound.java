package crocker.golf.bestball.domain.pga.tournament;

import crocker.golf.bestball.domain.enums.pga.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class TournamentRound {

    private UUID tournamentId;
    private UUID roundId;
    private Integer roundNumber;
    private Status roundStatus;

    private List<PlayerRound> playerRounds;
}
