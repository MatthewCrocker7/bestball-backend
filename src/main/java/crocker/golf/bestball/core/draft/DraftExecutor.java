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

        List<Team> matthewTeam = teams.stream().filter(team -> team.getUserId().toString().equals("cf8217b0-d23c-4a0d-ad96-90114b0e3ece"))
                .collect(Collectors.toList());

        if(!matthewTeam.isEmpty()) {
            List<Team> dentynTeam = teams.stream().filter(team -> team.getUserId().toString().equals("e7496ab2-d3b3-4601-8f7f-e66ec0a3aacd"))
                    .collect(Collectors.toList());
            List<Team> jonTeam = teams.stream().filter(team -> team.getUserId().toString().equals("082c3dc5-8945-4de1-940b-65e488645187"))
                    .collect(Collectors.toList());
            List<Team> drewTeam = teams.stream().filter(team -> team.getUserId().toString().equals("f829ac9a-fb9a-4c00-ba3c-73208960b25e"))
                    .collect(Collectors.toList());
            List<Team> mattTeam = teams.stream().filter(team -> team.getUserId().toString().equals("870ccfee-ce05-42ad-96d9-9dfcf538b26a"))
                    .collect(Collectors.toList());
            List<Team> zackTeam = teams.stream().filter(team -> team.getUserId().toString().equals("87a0b3d2-8401-4c96-9f9c-43da9f51292c"))
                    .collect(Collectors.toList());
            List<Team> connorTeam = teams.stream().filter(team -> team.getUserId().toString().equals("cfd9418d-7b3d-459f-9716-ae950e06b059"))
                    .collect(Collectors.toList());
            List<Team> shayneTeam = teams.stream().filter(team -> team.getUserId().toString().equals("a797385d-cf3c-42be-9316-9ec1d9a6d657"))
                    .collect(Collectors.toList());
            List<Team> donnyTeam = teams.stream().filter(team -> team.getUserId().toString().equals("a838467f-b498-44e4-8a54-8c140408f31c"))
                    .collect(Collectors.toList());

            jonTeam.get(0).setDraftPick(3);
            donnyTeam.get(0).setDraftPick(5);
            connorTeam.get(0).setDraftPick(4);
            zackTeam.get(0).setDraftPick(8);
            shayneTeam.get(0).setDraftPick(9);
            mattTeam.get(0).setDraftPick(7);
            drewTeam.get(0).setDraftPick(2);
            matthewTeam.get(0).setDraftPick(6);
            dentynTeam.get(0).setDraftPick(1);


        } else {
            Collections.shuffle(teams);
            for(int i = 1; i <= numPlayers; i++) {
                teams.get(i-1).setDraftPick(i);
            }
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
