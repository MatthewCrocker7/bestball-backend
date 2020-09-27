package crocker.golf.bestball.domain.pga.sports.radar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportsRadarTournamentRoundSummaryDto {

    @JsonProperty("id")
    private String roundId;

    @JsonProperty("number")
    private Integer roundNumber;

    @JsonProperty("status")
    private String roundStatus;

    public SportsRadarTournamentRoundSummaryDto() { super(); }
}
