package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.pga.Tournament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private UUID gameId;
    private String email;
    private String gameType;
    private ZonedDateTime draftDate;

    private BigDecimal buyIn;
    private Integer numPlayers;
    private UUID tournamentId;
}
