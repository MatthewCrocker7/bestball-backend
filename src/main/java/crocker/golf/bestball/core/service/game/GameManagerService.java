package crocker.golf.bestball.core.service.game;

import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.enums.game.GameState;
import crocker.golf.bestball.domain.enums.game.ScoreType;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.round.TeamRound;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.HoleScore;
import crocker.golf.bestball.domain.pga.tournament.PlayerRound;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class GameManagerService {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerService.class);

    private GameRepository gameRepository;
    private UserRepository userRepository;
    private PgaRepository pgaRepository;

    public GameManagerService(GameRepository gameRepository, UserRepository userRepository, PgaRepository pgaRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.pgaRepository = pgaRepository;
    }

    public Game loadGame(RequestDto requestDto) {
        UUID gameId = UUID.fromString(requestDto.getGameId());
        Game game = gameRepository.getLatestGameByGameId(gameId);
        UserCredentials userCredentials = userRepository.findByEmail(requestDto.getEmail());

        if (game.getGameState() == GameState.NOT_STARTED) {
            return game;
        }
        return enrichGame(game, userCredentials);
    }

    public void updateTeamScores() {
        List<Tournament> tournaments = getInProgressTournaments();

        tournaments.forEach(tournament -> {
            List<PlayerRound> playerRounds = pgaRepository.getPlayerRoundsByTournamentId(tournament.getTournamentId());
            List<Team> teams = gameRepository.getTeamsByTournamentId(tournament.getTournamentId());

            teams.forEach(team -> enrichTeamRounds(team, playerRounds));
            logger.info("Latest team round scores calculated for tournament {}", tournament.getName());

            List<TeamRound> allTeamRounds = new ArrayList<>();
            teams.forEach(team -> allTeamRounds.addAll(team.getTeamRounds()));
            gameRepository.updateTeamRounds(allTeamRounds);
        });
    }

    private void enrichTeamRounds(Team team, List<PlayerRound> playerRounds) {
        List<TeamRound> teamRounds = new ArrayList<>();
        for(int roundNumber = 1; roundNumber <= 4; roundNumber++) {
            TeamRound teamRound = getTeamRound(team, playerRounds, roundNumber);

            if(teamRound != null) teamRounds.add(teamRound);
        }

        team.setTeamRounds(teamRounds);
    }

    private TeamRound getTeamRound(Team team, List<PlayerRound> playerRounds, int roundNumber) {
        List<PlayerRound> rounds = playerRounds.stream()
                .filter(playerRound -> playerRound.getRoundNumber() == roundNumber && playerIsOnTeam(team.getGolfersAsList(), playerRound.getPlayerId()))
                .collect(Collectors.toList());

        TeamRound teamRound = TeamRound.builder()
                .teamId(team.getTeamId())
                .gameId(team.getGameId())
                .tournamentId(team.getTournamentId())
                .roundNumber(roundNumber)
                .build();

        if (rounds.isEmpty()) {
            return null;
        } else {
            teamRound.setRoundId(rounds.get(0).getRoundId());
        }

        enrichScores(teamRound, rounds);

        return teamRound;
    }

    private void enrichScores(TeamRound teamRound, List<PlayerRound> playerRounds) {
        List<HoleScore> teamScores = calculateTeamScores(playerRounds);

        int toPar;
        int strokes;
        int frontNine;
        int backNine;

        strokes = teamScores.stream().mapToInt(HoleScore::getStrokes).sum();
        toPar = strokes - teamScores.stream()
                .filter(holeScore -> holeScore.getScoreType() != ScoreType.TBD)
                .mapToInt(HoleScore::getPar).sum();

        frontNine = teamScores.stream().filter(holeScore -> holeScore.getHoleNumber() <= 9)
                .mapToInt(HoleScore::getStrokes).sum();
        backNine = teamScores.stream().filter(holeScore -> holeScore.getHoleNumber() > 9)
                .mapToInt(HoleScore::getStrokes).sum();

        teamRound.setHoleScores(teamScores);
        teamRound.setToPar(toPar);
        teamRound.setStrokes(strokes);
        teamRound.setFrontNine(frontNine);
        teamRound.setBackNine(backNine);
    }

    private List<HoleScore> calculateTeamScores(List<PlayerRound> rounds) {
        HashMap<Integer, HoleScore> teamScores = new HashMap<>();

        rounds.forEach(playerRound -> playerRound.getScores().forEach(holeScore -> {
            if (teamScores.containsKey(holeScore.getHoleNumber())) {
                teamScores.put(holeScore.getHoleNumber(), getLowestScore(teamScores, holeScore));
            } else {
                teamScores.put(holeScore.getHoleNumber(), holeScore);
            }
        }));

        return teamScores.keySet().stream().map(teamScores::get)
                .collect(Collectors.toList());
    }

    private HoleScore getLowestScore(HashMap<Integer, HoleScore> teamScores, HoleScore holeScore) {
        HoleScore currentBestScore = teamScores.get(holeScore.getHoleNumber());
        if (holeScore.getScoreType() != ScoreType.TBD) {
            if (currentBestScore.getScoreType() == ScoreType.TBD || currentBestScore.getStrokes() > holeScore.getStrokes()) {
                return holeScore;
            }
        }
        return currentBestScore;
    }

    private boolean playerIsOnTeam(List<PgaPlayer> players, UUID playerId) {
        return players.stream().anyMatch(pgaPlayer -> pgaPlayer.getPlayerId().equals(playerId));
    }

    private Game enrichGame(Game game, UserCredentials userCredentials) {
        Game enrichedGame = Game.builder()
                .gameId(game.getGameId())
                .gameState(game.getGameState())
                .gameVersion(game.getGameVersion())
                .gameType(game.getGameType())
                .draftId(game.getDraftId())
                .tournament(game.getTournament())
                .numPlayers(game.getNumPlayers())
                .buyIn(game.getBuyIn())
                .moneyPot(game.getMoneyPot())
                .build();

        enrichTeams(enrichedGame);

        logger.info("Enriched game {} loaded for {}", game.getGameId(), userCredentials.getEmail());
        return enrichedGame;
    }

    private void enrichTeams(Game game) {
        List<Team> teams = gameRepository.getTeamsByDraftId(game.getDraftId());
        List<PlayerRound> playerRounds = pgaRepository.getPlayerRoundsByTournamentId(game.getTournament().getTournamentId());
        List<TeamRound> allTeamRounds = gameRepository.getTeamRoundsByGameId(game.getGameId());

        teams.forEach(team -> {
            enrichPlayerRounds(team.getGolferOne(), playerRounds);
            enrichPlayerRounds(team.getGolferTwo(), playerRounds);
            enrichPlayerRounds(team.getGolferThree(), playerRounds);
            enrichPlayerRounds(team.getGolferFour(), playerRounds);

            List<TeamRound> teamRounds = allTeamRounds.stream()
                    .filter(teamRound -> teamRound.getTeamId().equals(team.getTeamId()))
                    .collect(Collectors.toList());

            team.setTeamRounds(teamRounds);
        });

        game.setTeams(teams);
    }

    private void enrichPlayerRounds(PgaPlayer pgaPlayer, List<PlayerRound> allPlayerRounds) {
        List<PlayerRound> playerRounds = allPlayerRounds.stream()
                .filter(playerRound -> playerRound.getPlayerId().equals(pgaPlayer.getPlayerId()))
                .collect(Collectors.toList());

        pgaPlayer.setRounds(playerRounds);
    }

    private List<Tournament> getInProgressTournaments() {
        List<Tournament> tournaments = pgaRepository.getAllTournaments();
        return tournaments.stream()
                .filter(tournament -> tournament.getTournamentState() == TournamentState.IN_PROGRESS)
                .collect(Collectors.toList());
    }

}
