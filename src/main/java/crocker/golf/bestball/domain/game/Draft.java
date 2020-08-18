package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Draft {

    private UUID draftId;
    private DraftState draftState;

    private LocalDateTime startTime;
    private ZoneId timeZone;


    private List<PgaPlayer> pgaPlayers;


}
