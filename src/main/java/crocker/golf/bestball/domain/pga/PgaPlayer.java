package crocker.golf.bestball.domain.pga;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PgaPlayer {
    private long playerId;
    private int rank;
    private String playerName;
}
