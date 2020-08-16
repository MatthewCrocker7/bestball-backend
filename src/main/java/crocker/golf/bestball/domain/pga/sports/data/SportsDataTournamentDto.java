package crocker.golf.bestball.domain.pga.sports.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class SportsDataTournamentDto {

    @JsonProperty("TournamentID")
    private Long tournamentId;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("StartDate")
    private LocalDateTime startDate;

    @JsonProperty("EndDate")
    private LocalDateTime endDate;

    @JsonProperty("StartDateTime")
    private LocalDateTime startDateTime;

    @JsonProperty("IsOver")
    private boolean isOver;

    @JsonProperty("IsInProgress")
    private boolean isInProgress;

    @JsonProperty("Venue")
    private String venue;

    @JsonProperty("Location")
    private String location;

    @JsonProperty("Par")
    private Integer par;

    @JsonProperty("Yards")
    private Integer yards;

    @JsonProperty("Purse")
    private BigDecimal purse;

    @JsonProperty("Canceled")
    private boolean canceled;

    @JsonProperty("TimeZone")
    private String timeZone;

    @JsonProperty("SportRadarTournamentID")
    private UUID sportRadarTournamentID;
}
