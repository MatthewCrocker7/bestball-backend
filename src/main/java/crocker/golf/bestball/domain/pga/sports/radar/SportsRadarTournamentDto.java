package crocker.golf.bestball.domain.pga.sports.radar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportsRadarTournamentDto {

    @JsonProperty("id")
    private String tournamentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("purse")
    private BigDecimal purse;

    @JsonProperty("winning_share")
    private BigDecimal winningShare;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("points")
    private Long points;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("course_timezone")
    private String courseTimezone;

    public SportsRadarTournamentDto() { super(); }
}
