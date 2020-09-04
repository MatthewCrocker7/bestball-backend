package crocker.golf.bestball.domain.enums.game;

public enum RoundNumber {
    ROUND_ONE(1),
    ROUND_TWO(2),
    ROUND_THREE(3),
    ROUND_FOUR(4);

    private int round;

    RoundNumber(int round) {
        this.round = round;
    }
}
