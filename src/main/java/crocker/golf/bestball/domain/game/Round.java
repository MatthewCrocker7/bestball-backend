package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.enums.RoundNumber;
import crocker.golf.bestball.domain.game.Hole;

import java.util.List;

public class Round {

    private List<Hole> holes;
    private RoundNumber roundNumber;
    private int par;
    private int toPar;
    private int totalScore;
}
