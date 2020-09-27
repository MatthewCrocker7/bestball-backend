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
public class SportsRadarTournamentRoundDto {

    @JsonProperty("id")
    private String tournamentId;

    @JsonProperty("name")
    private String tournamentName;

    @JsonProperty("round")
    private SportsRadarTournamentRoundScoresDto roundScoresDto;

    public SportsRadarTournamentRoundDto() { super(); }
}
