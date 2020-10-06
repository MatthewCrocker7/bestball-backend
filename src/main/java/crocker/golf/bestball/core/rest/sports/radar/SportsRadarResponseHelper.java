package crocker.golf.bestball.core.rest.sports.radar;

import crocker.golf.bestball.domain.enums.game.ScoreType;
import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.Status;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.sports.radar.*;
import crocker.golf.bestball.domain.pga.tournament.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SportsRadarResponseHelper {

    private static final Logger logger = LoggerFactory.getLogger(SportsRadarResponseHelper.class);

    public List<PgaPlayer> mapResponseToRankings(SportsRadarWorldGolfRankingDto sportsRadarWorldGolfRankingDto) {
        return sportsRadarWorldGolfRankingDto.getPlayers().stream()
                .map(pgaPlayerDto ->
                    PgaPlayer.builder()
                        .playerId(UUID.fromString(pgaPlayerDto.getPlayerId()))
                        .playerName(getFullName(pgaPlayerDto))
                        .rank(pgaPlayerDto.getRank())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Tournament> mapResponseToTournaments(SportsRadarScheduleDto scheduleDto) {
        return scheduleDto.getTournaments().stream()
                .filter(tournamentDto -> getEventType(tournamentDto) == EventType.STROKE)
                .map(tournamentDto ->
                    Tournament.builder()
                        .tournamentId(UUID.fromString(tournamentDto.getTournamentId()))
                        .eventType(getEventType(tournamentDto))
                        .season(scheduleDto.getSeasonDto().getYear())
                        .tournamentState(getTournamentState(tournamentDto))
                        .name(tournamentDto.getName())
                        .startDate(getStartDate(tournamentDto))
                        .endDate(tournamentDto.getEndDate())
                        .build())
                .collect(Collectors.toList());
    }

    public TournamentSummary mapResponseToTournamentSummary(SportsRadarTournamentSummaryDto summaryDto) {
        return TournamentSummary.builder()
                .tournamentId(UUID.fromString(summaryDto.getTournamentId()))
                .tournamentStatus(mapResponseToStatus(summaryDto.getStatus()))
                .tournamentCourses(mapResponseToTournamentCourses(summaryDto))
                .tournamentRounds(mapResponseToTournamentRounds(summaryDto))
                .build();
    }

    public TournamentRound mapResponseToTournamentRound(SportsRadarTournamentRoundDto roundDto) {
        return TournamentRound.builder()
                .tournamentId(UUID.fromString(roundDto.getTournamentId()))
                .roundId(UUID.fromString(roundDto.getRoundScoresDto().getRoundId()))
                .roundNumber(roundDto.getRoundScoresDto().getRoundNumber())
                .roundStatus(mapResponseToStatus(roundDto.getRoundScoresDto().getRoundStatus()))
                .playerRounds(mapResponseToPlayerRounds(roundDto))
                .build();
    }

    private List<PlayerRound> mapResponseToPlayerRounds(SportsRadarTournamentRoundDto roundDto) {
        return roundDto.getRoundScoresDto().getPlayerRoundDtos().stream()
                .map(playerRoundDto ->
                        PlayerRound.builder()
                        .playerId(UUID.fromString(playerRoundDto.getPlayerId()))
                        .tournamentId(UUID.fromString(roundDto.getTournamentId()))
                        .roundId(UUID.fromString(roundDto.getRoundScoresDto().getRoundId()))
                        .roundNumber(roundDto.getRoundScoresDto().getRoundNumber())
                        .courseId(UUID.fromString(playerRoundDto.getCourse().getCourseId()))
                        .toPar(playerRoundDto.getToPar())
                        .thru(playerRoundDto.getThru())
                        .strokes(playerRoundDto.getStrokes())
                        .scores(mapResponseToHoleScores(playerRoundDto))
                        .build())
                .collect(Collectors.toList());
    }

    private List<HoleScore> mapResponseToHoleScores(SportsRadarTournamentPlayerRoundDto playerRoundDto) {
        return playerRoundDto.getScores().stream()
                .map(scoreDto ->
                        HoleScore.builder()
                        .holeNumber(scoreDto.getHoleNumber())
                        .par(scoreDto.getPar())
                        .yardage(scoreDto.getYardage())
                        .strokes(scoreDto.getStrokes())
                        .scoreType(ScoreType.getScoreType(scoreDto.getPar(), scoreDto.getStrokes()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<TournamentCourse> mapResponseToTournamentCourses(SportsRadarTournamentSummaryDto summaryDto) {
        return summaryDto.getVenue().getCourses().stream()
                .map(courseDto ->
                        TournamentCourse.builder()
                        .tournamentId(UUID.fromString(summaryDto.getTournamentId()))
                        .courseId(UUID.fromString(courseDto.getCourseId()))
                        .courseName(courseDto.getName())
                        .yardage(courseDto.getYardage())
                        .par(courseDto.getPar())
                        .courseHoles(mapResponseToCourseHoles(courseDto.getHoles()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<CourseHole> mapResponseToCourseHoles(List<SportsRadarCourseHoleDto> holeDtos) {
        return holeDtos.stream()
                .map(holeDto ->
                        CourseHole.builder()
                        .holeNumber(holeDto.getHoleNumber())
                        .par(holeDto.getPar())
                        .yardage(holeDto.getYardage())
                        .build()
                ).collect(Collectors.toList());
    }

    private List<TournamentRound> mapResponseToTournamentRounds(SportsRadarTournamentSummaryDto summaryDto) {
        return summaryDto.getRounds().stream()
                .map(round ->
                        TournamentRound.builder()
                        .tournamentId(UUID.fromString(summaryDto.getTournamentId()))
                        .roundId(UUID.fromString(round.getRoundId()))
                        .roundNumber(round.getRoundNumber())
                        .roundStatus(mapResponseToStatus(round.getRoundStatus()))
                        .build()
                ).collect(Collectors.toList());
    }

    private Status mapResponseToStatus(String roundStatusDto) {
        switch (roundStatusDto) {
            case "scheduled":
                return Status.SCHEDULED;
            case "inprogress":
                return Status.IN_PROGRESS;
            case "delayed":
                return Status.DELAYED;
            case "suspended":
                return Status.SUSPENDED;
            case "cancelled":
                return Status.CANCELLED;
            case "complete":
                return Status.COMPLETE;
            case "closed":
                return Status.CLOSED;
            case "reopened":
                return Status.REOPENED;
        }
        return Status.CLOSED;
    }

    private String getFullName(SportsRadarPgaPlayerDto pgaPlayerDto) {
        return pgaPlayerDto.getFirstName() + " " + pgaPlayerDto.getLastName();
    }

    private String getFullName(SportsRadarTournamentPlayerRoundDto playerRoundDto) {
        return playerRoundDto.getFirstName() + " " + playerRoundDto.getLastName();
    }

    private EventType getEventType(SportsRadarTournamentDto tournamentDto) {
        return EventType.valueOf(tournamentDto.getEventType().toUpperCase());
    }

    private TournamentState getTournamentState(SportsRadarTournamentDto tournamentDto) {

        ZoneId courseTimeZone;
        if (tournamentDto.getCourseTimezone() == null) {
            courseTimeZone = ZoneId.of("UTC");
        } else {
            courseTimeZone = ZoneId.of(tournamentDto.getCourseTimezone());
        }

        LocalDate startDate  = tournamentDto.getStartDate();

        ZonedDateTime courseStartDateTime = startDate.atStartOfDay(courseTimeZone);
        ZonedDateTime currentTime = ZonedDateTime.now(courseTimeZone);

        if (currentTime.isBefore(courseStartDateTime)) {
            return TournamentState.NOT_STARTED;
        } else if (currentTime.isAfter(courseStartDateTime) && currentTime.isBefore(courseStartDateTime.plusDays(7))){
            return TournamentState.IN_PROGRESS;
        } else {
            return TournamentState.COMPLETE;
        }
    }

    private LocalDateTime getStartDate(SportsRadarTournamentDto tournamentDto) {
        // ZoneId courseTimeZone = ZoneId.of(tournamentDto.getCourseTimezone());
        LocalDate startDate  = tournamentDto.getStartDate();
        return startDate.atStartOfDay();

        // ZonedDateTime courseStartDateTime = startDate.atStartOfDay(courseTimeZone);
        // return courseStartDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }
}
