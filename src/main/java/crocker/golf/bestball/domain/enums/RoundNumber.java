package crocker.golf.bestball.domain.enums;

public enum RoundNumber {
    ROUND_ONE(1),
    ROUND_TWO(2),
    ROUND_THREE(3),
    ROUND_FOUR(4);

    private int round;

    private RoundNumber(int round) {
        this.round = round;
    }
}
