package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.game.participant.PlayerParticipant;
import crocker.golf.bestball.domain.game.participant.UserParticipant;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public class Team {

    private UUID teamId;

    private UserParticipant userParticipant;
    List<PlayerParticipant> playerParticipants;

}
