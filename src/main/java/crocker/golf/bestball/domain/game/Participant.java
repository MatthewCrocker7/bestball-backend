package crocker.golf.bestball.domain.game;

import java.util.List;
import java.util.UUID;

abstract class Participant {

    private UUID participantId;
    private List<Round> rounds;
    private int totalScore;
    private int toPar;
}
