package crocker.golf.bestball.domain.game;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public class Game {

    private UUID gameId;
    private List<Team> teams;
    private BigDecimal pot;
}
