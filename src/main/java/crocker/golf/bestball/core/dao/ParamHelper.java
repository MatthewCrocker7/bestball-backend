package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.CourseHole;
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

    private static byte[] convertToByteArray(List<CourseHole> courseHoles) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(courseHoles);
        return byteArrayOutputStream.toByteArray();
    }
}
