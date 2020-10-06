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
public class SportsRadarTournamentSummaryDto {

    @JsonProperty("id")
    private String tournamentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("venue")
    private SportsRadarVenueDto venue;

    @JsonProperty("rounds")
    private List<SportsRadarTournamentRoundSummaryDto> rounds;

    @JsonProperty("field")
    private List<SportsRadarPgaPlayerDto> field;

    public SportsRadarTournamentSummaryDto() { super(); }
}
