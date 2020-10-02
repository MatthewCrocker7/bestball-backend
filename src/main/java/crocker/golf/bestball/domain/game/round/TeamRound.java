package crocker.golf.bestball.domain.game.round;

import crocker.golf.bestball.domain.pga.tournament.HoleScore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Setter
@Getter
public class TeamRound {

    private UUID teamId;
    private UUID gameId;
    private UUID tournamentId;
    private UUID roundId;
    private Integer roundNumber;

    private Integer toPar;
    private Integer strokes;
    private List<HoleScore> holeScores;

    private Integer frontNine;
    private Integer backNine;
}
