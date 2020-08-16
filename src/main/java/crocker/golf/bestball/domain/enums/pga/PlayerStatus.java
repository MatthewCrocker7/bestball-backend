package crocker.golf.bestball.domain.enums.pga;

public enum PlayerStatus {
    CUT("The player didn't make the tournament cut"),
    WD("The player has withdrawn from the tournament"),
    MDF("The player made the cut, but did not finish the tournament"),
    DQ("The player was disqualified"),
    DNS("The player was scheduled to compete, but never started");

    private String description;

    PlayerStatus(String description) {
        this.description = description;
    }
}
