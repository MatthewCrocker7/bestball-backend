package crocker.golf.bestball.domain.enums.game;

public enum ScoreType {
    PAR,
    BIRDIE,
    EAGLE,
    BOGEY,
    DOUBLE_BOGEY,
    TRIPLE_BOGEY,
    PLUS_TRIPLE,
    TBD;

    public static ScoreType getScoreType(Integer par, Integer strokes) {
        if (strokes == 0) {
            return ScoreType.TBD;
        }
        int toPar = strokes - par;

        switch (toPar) {
            case -2:
                return ScoreType.EAGLE;
            case -1:
                return ScoreType.BIRDIE;
            case 0:
                return ScoreType.PAR;
            case 1:
                return ScoreType.BOGEY;
            case 2:
                return ScoreType.DOUBLE_BOGEY;
            case 3:
                return ScoreType.TRIPLE_BOGEY;
            default:
                return ScoreType.PLUS_TRIPLE;
        }
    }
}
