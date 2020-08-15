package crocker.golf.bestball.domain.game.participant;

import crocker.golf.bestball.domain.game.Round;

import java.util.List;
import java.util.UUID;

abstract class Participant {

    private Round round;
    private Integer totalScore;
    private Integer frontNine;
    private Integer backNine;
    private Integer toPar;
}
