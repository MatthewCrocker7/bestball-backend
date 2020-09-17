package crocker.golf.bestball.domain.game.draft;

import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Draft {
    @NonNull
    private UUID draftId;
    @NonNull
    private DraftState draftState;
    @NonNull
    private Integer draftVersion;
    @NonNull
    private LocalDateTime startTime;
    @NonNull
    private Integer currentPick;

    private List<PgaPlayer> availablePgaPlayers;
    private Map<Integer, UserInfo> draftOrder;

    private List<Team> teams;
    private Integer maxPlayers;
}
