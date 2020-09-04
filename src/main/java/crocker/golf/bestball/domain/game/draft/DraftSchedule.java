package crocker.golf.bestball.domain.game.draft;

import crocker.golf.bestball.domain.enums.game.ReleaseStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DraftSchedule {

    private UUID draftId;
    private ReleaseStatus releaseStatus;
    private LocalDateTime releaseTime;
}
