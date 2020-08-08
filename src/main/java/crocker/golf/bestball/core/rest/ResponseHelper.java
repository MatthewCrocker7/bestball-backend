package crocker.golf.bestball.core.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.PgaPlayerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseHelper {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHelper.class);

    public static List<PgaPlayer> mapResponseToRankings(List<PgaPlayerDto> list) {
        return list.stream()
                .filter(ResponseHelper::filterPgaPlayerDto)
                .map(pgaPlayerDto ->
                    PgaPlayer.builder()
                        .playerId(pgaPlayerDto.getPlayerID())
                        .playerName(pgaPlayerDto.getName())
                        .rank(getWorldGolfRank(pgaPlayerDto))
                        .build()
                ).collect(Collectors.toList());
    }

    public static List<PgaPlayerDto> test(List<Object> list) {
        ObjectMapper mapper = new ObjectMapper();
        return list.stream().map(item -> mapper.convertValue(item, PgaPlayerDto.class))
                .collect(Collectors.toList());
    }

    private static boolean filterPgaPlayerDto(PgaPlayerDto pgaPlayerDto) {
        if (pgaPlayerDto.getWorldGolfRank() == null) {
            return false;
        }

        return pgaPlayerDto.getWorldGolfRank() <= 100;
    }

    private static int getWorldGolfRank(PgaPlayerDto pgaPlayerDto) {
        if (pgaPlayerDto.getWorldGolfRank() != null) {
            return pgaPlayerDto.getWorldGolfRank();
        } else {
            logger.error("Supplied PgaPlayer rank from API service is null for {}", pgaPlayerDto.getPlayerID());
            return 9999;
        }
    }
}
