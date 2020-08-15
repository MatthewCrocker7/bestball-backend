package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.enums.GameType;
import crocker.golf.bestball.domain.pga.Tournament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private String email;
    private String gameType;
    private LocalDate draftDate;
    private LocalTime draftTime;
    private ZoneId timeZone;

    private BigDecimal buyIn;
    private BigDecimal numPlayers;
    private Tournament tournament; // might be better to just pass tournament id through game DTO
}
