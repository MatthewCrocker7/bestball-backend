package crocker.golf.bestball.domain.pga.sports.radar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportsRadarPgaPlayerRankingDto {

    @JsonProperty("id")
    private String playerId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("prior_rank")
    private Integer priorRank;

    @JsonProperty("country")
    private String country;

    public SportsRadarPgaPlayerRankingDto(){ super(); }
}
