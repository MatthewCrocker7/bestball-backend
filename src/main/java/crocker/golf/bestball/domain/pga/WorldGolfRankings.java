package crocker.golf.bestball.domain.pga;

import lombok.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class WorldGolfRankings {

    private Map<Integer, PgaPlayer> worldRankings;


    public Map<Integer, PgaPlayer> getWorldRankings() {
        return worldRankings;
    }
}
