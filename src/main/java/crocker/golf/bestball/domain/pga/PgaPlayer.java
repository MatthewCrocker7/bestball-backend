package crocker.golf.bestball.domain.pga;

import crocker.golf.bestball.domain.pga.tournament.PlayerRound;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PgaPlayer {
    private UUID playerId;
    private Integer rank;
    private String playerName;

    List<PlayerRound> rounds;
}
