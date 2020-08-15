package crocker.golf.bestball.domain.game.participant;

import lombok.Builder;

import java.util.UUID;

@Builder
public class UserParticipant extends Participant{

    private UUID userId;
}
