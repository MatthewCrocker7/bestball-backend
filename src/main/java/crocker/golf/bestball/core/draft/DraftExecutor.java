package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.enums.game.ReleaseStatus;
import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DraftExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DraftTasklet.class);

    private DraftScheduler draftScheduler;
    private DraftRepository draftRepository;
    private GameRepository gameRepository;
    private PgaRepository pgaRepository;
    private UserRepository userRepository;

    public DraftExecutor(DraftScheduler draftScheduler, DraftRepository draftRepository, GameRepository gameRepository, PgaRepository pgaRepository, UserRepository userRepository) {
        this.draftScheduler = draftScheduler;
        this.draftRepository = draftRepository;
        this.gameRepository = gameRepository;
        this.pgaRepository = pgaRepository;
        this.userRepository = userRepository;
    }

    public void execute(DraftSchedule draftSchedule) {
        logger.info("Executing draft {} at {}", draftSchedule.getDraftId(), draftSchedule.getReleaseTime());
        releaseDraftSchedule(draftSchedule);

        Draft draft = draftRepository.getLatestDraftById(draftSchedule.getDraftId());

        Draft updatedDraft = initiateDraft(draft);

        draftRepository.saveDraft(updatedDraft);

        // Users will log in, see available in progress draft
        // At this point draft should have order set up, and available players
    }

    private Draft initiateDraft(Draft draft) {
        Draft updatedDraft = Draft.builder()
                .draftId(draft.getDraftId())
                .draftVersion(draft.getDraftVersion() + 1)
                .draftState(DraftState.IN_PROGRESS)
                .startTime(draft.getStartTime())
                .currentPick(1)
                .build();

        determineDraftOrder(draft.getDraftId());

        saveDraftablePgaPlayers(draft.getDraftId());

        return updatedDraft;
    }

    private void determineDraftOrder(UUID draftId) {
        List<Team> teams = gameRepository.getTeamsByDraftId(draftId);
        int numPlayers = teams.size();
        Collections.shuffle(teams);
        for(int i = 1; i <= numPlayers; i++) {
            teams.get(i-1).setDraftPick(i);
        }

        List<UserInfo> draftOrder = teams.stream()
                .map(team -> {
                    UserCredentials user = userRepository.getUserByUserId(team.getUserId());
                    return UserInfo.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .email(user.getEmail())
                            .pickNumber(team.getDraftPick())
                            .build();
                })
                .map(userInfo -> Arrays.asList(
                        userInfo,
                        getUserInfoRound(userInfo, numPlayers, 2),
                        getUserInfoRound(userInfo, numPlayers, 3),
                        getUserInfoRound(userInfo, numPlayers, 4)
                ))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        draftRepository.saveDraftOrder(draftId, draftOrder);
        gameRepository.updateTeams(teams);
    }

    private UserInfo getUserInfoRound(UserInfo user, int numPlayers, int round) {
        int pickNumber = determinePickNumber(user, numPlayers, round);

        return UserInfo.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .pickNumber(pickNumber)
                .build();
    }

    private Integer determinePickNumber(UserInfo user, int numPlayers, int round) {
        //TODO: Unit test PLEASE
        int roundSum = numPlayers * (2 * round - 2) + 1;
        int roundPick;

        if(round == 1) {
            roundPick = user.getPickNumber();
        } else {
            roundPick = roundSum - determinePickNumber(user, numPlayers, round - 1);
        }

        return roundPick;
    }


    private void saveDraftablePgaPlayers(UUID draftId) {
        Game game = gameRepository.getLatestGameByDraftId(draftId);
        UUID tournamentId = game.getTournament().getTournamentId();
        List<PgaPlayer> tournamentField = pgaRepository.getTournamentField(tournamentId);
        List<PgaPlayer> worldRankings = pgaRepository.getWorldRankings();
        List<UUID> worldRankingsPlayerId = worldRankings.stream()
                .map(PgaPlayer::getPlayerId).collect(Collectors.toList());
        List<PgaPlayer> draftablePlayers = tournamentField.stream()
                .filter(pgaPlayer -> worldRankingsPlayerId.contains(pgaPlayer.getPlayerId()))
                .collect(Collectors.toList());
        draftRepository.saveDraftablePgaPlayers(draftId, draftablePlayers);
    }

    private void releaseDraftSchedule(DraftSchedule draftSchedule) {
        draftScheduler.getTaskMap().remove(draftSchedule.getDraftId());

        draftRepository.updateReleaseStatus(getReleasedDraft(draftSchedule));
    }

    private DraftSchedule getReleasedDraft(DraftSchedule draftSchedule) {
        return DraftSchedule.builder()
                .draftId(draftSchedule.getDraftId())
                .releaseTime(draftSchedule.getReleaseTime())
                .releaseStatus(ReleaseStatus.RELEASED)
                .build();
    }
}
