package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.game.draft.Draft;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class TeamInfo {

    private UUID teamId;
    private Game game;
    private Draft draft;
}
