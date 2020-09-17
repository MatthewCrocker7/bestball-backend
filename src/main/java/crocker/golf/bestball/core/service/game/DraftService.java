package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DraftService {

    private static final Logger logger = LoggerFactory.getLogger(DraftService.class);

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

    public Draft draftPlayer(RequestDto requestDto, UUID playerId) {
        String email = requestDto.getEmail();
        UUID draftId = UUID.fromString(requestDto.getDraftId());
        UserCredentials userCredentials = userRepository.findByEmail(email);

        Draft draft = draftRepository.getLatestDraftById(draftId);
        Draft enrichedDraft = enrichDraft(draft, userCredentials);

        if (!isPlayerTurn(enrichedDraft, userCredentials)) {
            logger.error("It is not {} turn to pick for draft {}", userCredentials.getUserName(), enrichedDraft.getDraftId());
            throw new RuntimeException("Draft request by ineligible user");
        }

        PgaPlayer pgaPlayer = draftRepository.getPgaPlayerById(enrichedDraft.getDraftId(), playerId);


        updateTeam(enrichedDraft, pgaPlayer, userCredentials);
        updateAvailablePgaPlayers(enrichedDraft, pgaPlayer);

        enrichedDraft.setCurrentPick(enrichedDraft.getCurrentPick() + 1);
        enrichedDraft.setDraftVersion(enrichedDraft.getDraftVersion() + 1);

        checkForCompletion(enrichedDraft);

        draftRepository.saveDraft(enrichedDraft);

        return enrichedDraft;
    }

    private Draft enrichDraft(Draft draft, UserCredentials userCredentials) {
        Game game = gameRepository.getLatestGameByDraftId(draft.getDraftId());

        Draft enrichedDraft = Draft.builder()
                .draftId(draft.getDraftId())
                .draftState(draft.getDraftState())
                .draftVersion(draft.getDraftVersion())
                .startTime(draft.getStartTime())
                .currentPick(draft.getCurrentPick())
                .maxPlayers(game.getNumPlayers())
                .build();

        enrichAvailablePgaPlayers(enrichedDraft);
        enrichDraftOrderAndTeams(enrichedDraft);

        return enrichedDraft;
    }

    private void enrichAvailablePgaPlayers(Draft draft) {
        draft.setAvailablePgaPlayers(draftRepository.getDraftablePgaPlayersByDraftId(draft.getDraftId()));
    }

    private void enrichDraftOrderAndTeams(Draft draft) {
        List<UserInfo> users = draftRepository.getDraftOrderByDraftId(draft.getDraftId());

        Map<Integer, UserInfo> draftOrder = users.stream()
                .collect(Collectors.toMap(UserInfo::getPickNumber, user -> user));

        List<Team> teams = gameRepository.getTeamsByDraftId(draft.getDraftId());

        teams.forEach(team ->  {
            Optional<UserInfo> userInfo = users.stream().filter(user ->
                    user.getPickNumber() <= teams.size() && user.getUserId().equals(team.getUserId()))
                    .findFirst();

            if (userInfo.isPresent()) {
                team.setUserInfo(userInfo.get());
            } else {
                logger.error("Unable to find matching user for team {} and draft {}", team.getTeamId(), draft.getDraftId());
            }
        });

        draft.setDraftOrder(draftOrder);
        draft.setTeams(teams);
    }

    private boolean isPlayerTurn(Draft draft, UserCredentials userCredentials) {
        Integer currentPick = draft.getCurrentPick();

        return (draft.getDraftOrder().get(currentPick).getUserId().equals(userCredentials.getUserId()));
    }

    private void updateTeam(Draft draft, PgaPlayer pgaPlayer, UserCredentials user) {
        Optional<Team> optionalDraftingTeam = draft.getTeams().stream().filter(team -> team.getUserId().equals(user.getUserId()))
                .findFirst();
        List<Team> updatedTeams = draft.getTeams().stream().filter(team -> !team.getUserId().equals(user.getUserId()))
                .collect(Collectors.toList());
        Team draftingTeam;

        if (optionalDraftingTeam.isPresent()) {
            draftingTeam = optionalDraftingTeam.get();
        } else {
            logger.error("Drafting team for user {} is not present for draft {}", user.getUserId(), draft.getDraftId());
            throw new RuntimeException("Drafting team not present");
        }

        if (draftingTeam.getGolferOne() == null) {
            draftingTeam.setGolferOne(pgaPlayer);
        } else if (draftingTeam.getGolferTwo() == null) {
            draftingTeam.setGolferTwo(pgaPlayer);
        } else if (draftingTeam.getGolferThree() == null) {
            draftingTeam.setGolferThree(pgaPlayer);
        } else {
            draftingTeam.setGolferFour(pgaPlayer);
        }

        updatedTeams.add(draftingTeam);
        gameRepository.updateTeam(draftingTeam);
        draft.setTeams(updatedTeams);
    }

    private void updateAvailablePgaPlayers(Draft draft, PgaPlayer pgaPlayer) {
        draftRepository.draftPlayer(draft.getDraftId(), pgaPlayer);
        draft.setAvailablePgaPlayers(draftRepository.getDraftablePgaPlayersByDraftId(draft.getDraftId()));
    }

    private void checkForCompletion(Draft draft) {
        if (draft.getCurrentPick() > draft.getTeams().size() * 4) {
            draft.setDraftState(DraftState.COMPLETE);
        }
    }
}
