package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.enums.game.TeamRole;
import crocker.golf.bestball.domain.game.round.Round;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Team {

    private UUID teamId;
    private UUID userId;
    private UUID draftId;
    private UUID gameId;

    private TeamRole teamRole;
    private Integer draftPick;

    private UUID playerOneId;
    private UUID playerTwoId;
    private UUID playerThreeId;
    private UUID playerFourId;

    private Integer toPar;
    private Integer totalScore;

    private List<Round> rounds;

}
