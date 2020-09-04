package crocker.golf.bestball.domain.game.round;

import crocker.golf.bestball.domain.enums.game.RoundNumber;
import crocker.golf.bestball.domain.game.Hole;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
public abstract class Round {

    protected UUID roundId;
    protected UUID teamId;

    protected List<Hole> holes;
    protected RoundNumber roundNumber;

    protected Integer toPar;
    protected Integer totalScore;
    protected Integer frontNine;
    protected Integer backNine;
}
