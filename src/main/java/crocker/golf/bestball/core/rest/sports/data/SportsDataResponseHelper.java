package crocker.golf.bestball.core.rest.sports.data;

import crocker.golf.bestball.domain.enums.TournamentState;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.sports.data.SportsDataPgaPlayerDto;
import crocker.golf.bestball.domain.pga.Tournament;
import crocker.golf.bestball.domain.pga.sports.data.SportsDataTournamentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SportsDataResponseHelper {

    private static final Logger logger = LoggerFactory.getLogger(SportsDataResponseHelper.class);

    private static final Integer MAX_RANK = 100;

    public List<PgaPlayer> mapResponseToRankings(List<SportsDataPgaPlayerDto> list) {
        return list.stream()
                .filter(SportsDataResponseHelper::filterPgaPlayerDto)
                .map(sportsDataPgaPlayerDto ->
                    PgaPlayer.builder()
                        .playerId(sportsDataPgaPlayerDto.getPlayerId())
                        .playerName(sportsDataPgaPlayerDto.getName())
                        .rank(getWorldGolfRank(sportsDataPgaPlayerDto))
                        .build()
                ).collect(Collectors.toList());
    }

    public List<Tournament> mapResponseToTournaments(List<SportsDataTournamentDto> list) {
        return list.stream()
                .map(sportsDataTournamentDto ->
                    Tournament.builder()
                        .sportsDataTournamentId(sportsDataTournamentDto.getTournamentId())
                        .sportsRadarTournamentId(sportsDataTournamentDto.getSportRadarTournamentID())
                        .tournamentState(getTournamentState(sportsDataTournamentDto))
                        .name(sportsDataTournamentDto.getName())
                        .startDate(sportsDataTournamentDto.getStartDate())
                        .endDate(sportsDataTournamentDto.getEndDate())
                        .build()
                ).collect(Collectors.toList());
    }

    private static boolean filterPgaPlayerDto(SportsDataPgaPlayerDto sportsDataPgaPlayerDto) {
        if (sportsDataPgaPlayerDto.getWorldGolfRank() == null) {
            return false;
        }

        return sportsDataPgaPlayerDto.getWorldGolfRank() <= MAX_RANK;
    }

    private int getWorldGolfRank(SportsDataPgaPlayerDto sportsDataPgaPlayerDto) {
        if (sportsDataPgaPlayerDto.getWorldGolfRank() != null) {
            return sportsDataPgaPlayerDto.getWorldGolfRank();
        } else {
            logger.error("Supplied PgaPlayer rank from API service is null for {}", sportsDataPgaPlayerDto.getPlayerId());
            return 9999;
        }
    }

    private TournamentState getTournamentState(SportsDataTournamentDto sportsDataTournamentDto) {
        boolean inProgress = sportsDataTournamentDto.isInProgress();
        boolean isComplete = sportsDataTournamentDto.isOver();
        boolean isCancelled = sportsDataTournamentDto.isCanceled();

        if (isComplete) {
            return TournamentState.COMPLETE;
        } else if (inProgress) {
            return  TournamentState.IN_PROGRESS;
        } else if (isCancelled) {
            return TournamentState.CANCELLED;
        } else {
            return TournamentState.NOT_STARTED;
        }
    }
}
