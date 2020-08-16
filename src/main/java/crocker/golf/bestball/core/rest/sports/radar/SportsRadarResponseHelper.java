package crocker.golf.bestball.core.rest.sports.radar;

import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;
import crocker.golf.bestball.domain.pga.sports.radar.SportsRadarPgaPlayerDto;
import crocker.golf.bestball.domain.pga.sports.radar.SportsRadarScheduleDto;
import crocker.golf.bestball.domain.pga.sports.radar.SportsRadarTournamentDto;
import crocker.golf.bestball.domain.pga.sports.radar.SportsRadarWorldGolfRankingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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
                        .sportsRadarTournamentId(UUID.fromString(tournamentDto.getTournamentId()))
                        .eventType(getEventType(tournamentDto))
                        .season(scheduleDto.getSeasonDto().getYear())
                        .tournamentState(getTournamentState(tournamentDto))
                        .name(tournamentDto.getName())
                        .startDate(getStartDate(tournamentDto))
                        .endDate(tournamentDto.getEndDate())
                        .build())
                .collect(Collectors.toList());
    }

    private String getFullName(SportsRadarPgaPlayerDto pgaPlayerDto) {
        return pgaPlayerDto.getFirstName() + " " + pgaPlayerDto.getLastName();
    }

    private EventType getEventType(SportsRadarTournamentDto tournamentDto) {
        return EventType.valueOf(tournamentDto.getEventType().toUpperCase());
    }

    private TournamentState getTournamentState(SportsRadarTournamentDto tournamentDto) {

        if (tournamentDto.getCourseTimezone() == null) {
            logger.error("error");
        }

        ZoneId courseTimeZone = ZoneId.of(tournamentDto.getCourseTimezone());
        LocalDate startDate  = tournamentDto.getStartDate();

        ZonedDateTime courseStartDateTime = startDate.atStartOfDay(courseTimeZone);

        if (ZonedDateTime.now(courseTimeZone).isBefore(courseStartDateTime)) {
            return TournamentState.NOT_STARTED;
        } else {
            //TODO: This logic isn't complete. Doesn't account for cancelled or delayed tournaments
            return TournamentState.COMPLETE;
        }
    }

    private ZonedDateTime getStartDate(SportsRadarTournamentDto tournamentDto) {
        ZoneId courseTimeZone = ZoneId.of(tournamentDto.getCourseTimezone());
        LocalDate startDate  = tournamentDto.getStartDate();

        ZonedDateTime courseStartDateTime = startDate.atStartOfDay(courseTimeZone);
        return courseStartDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }
}
