package crocker.golf.bestball.domain.pga.tournament;

import crocker.golf.bestball.domain.enums.pga.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class PlayerRound {

    private UUID playerId;
    private UUID tournamentId;
    private UUID roundId;
    private Integer roundNumber;
    private UUID courseId;
    private String playerName;

    private Integer toPar;
    private Integer strokes;
    private Integer thru;
    private List<HoleScore> scores;

    private Integer frontNine;
    private Integer backNine;
}
