package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.user.UserInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DraftService {

    private DraftRepository draftRepository;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private PgaRepository pgaRepository;

    public DraftService(DraftRepository draftRepository, UserRepository userRepository, GameRepository gameRepository, PgaRepository pgaRepository) {
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.pgaRepository = pgaRepository;
    }

    public Draft loadDraft(RequestDto requestDto) {
        String email = requestDto.getEmail();
        UUID draftId = UUID.fromString(requestDto.getDraftId());
        UserCredentials userCredentials = userRepository.findByEmail(email);

        Draft draft = draftRepository.getLatestDraftById(draftId);

        return enrichDraft(draft, userCredentials);
    }

    private Draft enrichDraft(Draft draft, UserCredentials userCredentials) {
        Draft enrichedDraft = Draft.builder()
                .draftId(draft.getDraftId())
                .draftState(draft.getDraftState())
                .draftVersion(draft.getDraftVersion())
                .startTime(draft.getStartTime())
                .currentPick(draft.getCurrentPick())
                .build();

        enrichedDraft.setAvailablePgaPlayers(getAvailablePgaPlayers(draft.getDraftId()));
        enrichedDraft.setDraftOrder(getDraftOrder(draft.getDraftId()));

        return enrichedDraft;
    }

    private List<PgaPlayer> getAvailablePgaPlayers(UUID draftId) {
        return draftRepository.getDraftablePgaPlayersByDraftId(draftId);
    }

    private Map<Integer, UserInfo> getDraftOrder(UUID draftId) {
        return draftRepository.getDraftOrderByDraftId(draftId);
    }
}
