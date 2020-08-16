package crocker.golf.bestball.domain.pga;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PgaPlayer {
    private UUID playerId;
    private int rank;
    private String playerName;
}
