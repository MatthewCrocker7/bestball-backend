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
public class SportsRadarWorldGolfRankingDto {

    @JsonProperty("id")
    private String worldGolfRankingId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("status")
    private String status;

    @JsonProperty("season")
    private Integer season;

    @JsonProperty("players")
    private List<SportsRadarPgaPlayerDto> players;

    public SportsRadarWorldGolfRankingDto(){ super(); }

}
