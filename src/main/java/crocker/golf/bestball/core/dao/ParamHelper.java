package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.game.round.TeamRound;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.CourseHole;
import crocker.golf.bestball.domain.pga.tournament.PlayerRound;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class ParamHelper {

    private static final Logger logger = LoggerFactory.getLogger(ParamHelper.class);

    public static MapSqlParameterSource[] getPlayerParams(List<PgaPlayer> pgaPlayers) {
        return pgaPlayers.stream().map(pgaPlayer -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("playerId", pgaPlayer.getPlayerId());
            params.addValue("playerRank", pgaPlayer.getRank());
            params.addValue("playerName", pgaPlayer.getPlayerName());

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }

    public static MapSqlParameterSource[] getTournamentParams(List<Tournament> tournaments) {
        return tournaments.stream().map(tournament -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("tournamentId", tournament.getTournamentId());
            params.addValue("eventType", tournament.getEventType().name());
            params.addValue("season", tournament.getSeason());
            params.addValue("tournamentState", tournament.getTournamentState().name());
            params.addValue("tournamentName", tournament.getName());
            params.addValue("startDate", tournament.getStartDate());
            params.addValue("endDate", tournament.getEndDate());

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }

    public static MapSqlParameterSource[] getTournamentCourseParams(TournamentSummary tournamentSummary) {

        return tournamentSummary.getTournamentCourses().stream().map(tournamentCourse -> {
            try {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("tournamentId", tournamentSummary.getTournamentId());
                params.addValue("courseId", tournamentCourse.getCourseId());
                params.addValue("courseName", tournamentCourse.getCourseName());
                params.addValue("yardage", tournamentCourse.getYardage());
                params.addValue("par", tournamentCourse.getPar());
                params.addValue("holes", convertToByteArray(tournamentCourse.getCourseHoles()));

                return params;
            } catch (IOException e) {
                logger.error("Course holes is null for {}, can't convert to byte array", tournamentSummary.getTournamentId());
                throw new RuntimeException(e);
            }
        }).toArray(MapSqlParameterSource[]::new);

    }

    public static MapSqlParameterSource[] getTournamentRoundParams(TournamentSummary tournamentSummary) {
        return tournamentSummary.getTournamentRounds().stream().map(tournamentRound -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("tournamentId", tournamentSummary.getTournamentId());
            params.addValue("roundId", tournamentRound.getRoundId());
            params.addValue("roundNumber", tournamentRound.getRoundNumber());
            params.addValue("status", tournamentRound.getRoundStatus().name());

            return params;
        }).toArray(MapSqlParameterSource[]::new);
    }

    public static MapSqlParameterSource[] getPlayerRoundParams(List<PlayerRound> playerRounds) {
        return playerRounds.stream().map(playerRound -> {
            try {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("playerId", playerRound.getPlayerId());
                params.addValue("tournamentId", playerRound.getTournamentId());
                params.addValue("roundId", playerRound.getRoundId());
                params.addValue("roundNumber", playerRound.getRoundNumber());
                params.addValue("courseId", playerRound.getCourseId());
                params.addValue("toPar", playerRound.getToPar());
                params.addValue("thru", playerRound.getThru());
                params.addValue("strokes", playerRound.getStrokes());
                params.addValue("scores", convertToByteArray(playerRound.getScores()));

                return params;
            } catch (IOException e) {
                logger.error("Score is null for player {} during round {} of tournament {}", playerRound.getPlayerId(), playerRound.getRoundNumber(), playerRound.getTournamentId());
                throw new RuntimeException(e);
            }
        }).toArray(MapSqlParameterSource[]::new);
    }

    public static MapSqlParameterSource getNewTeamParams(Team team) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("teamId", team.getTeamId());
        params.addValue("userId", team.getUserId());
        params.addValue("gameId", team.getGameId());
        params.addValue("draftId", team.getDraftId());
        params.addValue("tournamentId", team.getTournamentId());
        params.addValue("teamRole", team.getTeamRole().name());

        return params;
    }

    public static MapSqlParameterSource getUpdateTeamParams(Team team) {
        MapSqlParameterSource params = getNewTeamParams(team);

        params.addValue("draftPick", team.getDraftPick());
        params.addValue("playerOneId", team.getGolferOne() != null ? team.getGolferOne().getPlayerId() : null);
        params.addValue("playerTwoId", team.getGolferTwo() != null ? team.getGolferTwo().getPlayerId() : null);
        params.addValue("playerThreeId", team.getGolferThree() != null ? team.getGolferThree().getPlayerId() : null);
        params.addValue("playerFourId", team.getGolferFour() != null ? team.getGolferFour().getPlayerId() : null);
        params.addValue("toPar", team.getToPar());
        params.addValue("totalStrokes", team.getTotalStrokes());

        return params;
    }

    public static MapSqlParameterSource[] getUpdateTeamsParams(List<Team> teams) {
        return teams.stream().map(ParamHelper::getUpdateTeamParams).toArray(MapSqlParameterSource[]::new);
    }

    public static MapSqlParameterSource[] getTeamRoundParams(List<TeamRound> teamRounds) {
        return teamRounds.stream().map(teamRound -> {
            try {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("teamId", teamRound.getTeamId());
                params.addValue("gameId", teamRound.getGameId());
                params.addValue("roundId", teamRound.getRoundId());
                params.addValue("tournamentId", teamRound.getTournamentId());
                params.addValue("roundNumber", teamRound.getRoundNumber());
                params.addValue("toPar", teamRound.getToPar());
                params.addValue("strokes", teamRound.getStrokes());
                params.addValue("frontNine", teamRound.getFrontNine());
                params.addValue("backNine", teamRound.getBackNine());
                params.addValue("scores", convertToByteArray(teamRound.getHoleScores()));

                return params;
            } catch (IOException e) {
                logger.error("Score is null for team {} during round {} of tournament {}", teamRound.getTeamId(), teamRound.getRoundNumber(), teamRound.getTournamentId());
                throw new RuntimeException(e);
            }

        }).toArray(MapSqlParameterSource[]::new);
    }

    private static byte[] convertToByteArray(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }
}
