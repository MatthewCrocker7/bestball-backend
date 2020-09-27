package crocker.golf.bestball.domain.game.round;

import crocker.golf.bestball.domain.enums.game.RoundNumber;
import crocker.golf.bestball.domain.game.TeamHoleScore;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public class TeamRound {

    protected UUID roundId;
    protected UUID teamId;

    protected List<TeamHoleScore> teamHoleScores;
    protected RoundNumber roundNumber;

    protected Integer toPar;
    protected Integer strokes;
    protected Integer frontNine;
    protected Integer backNine;
}
