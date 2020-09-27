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
public class SportsRadarTournamentPlayerRoundDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("id")
    private String playerId;

    @JsonProperty("score")
    private Integer toPar;

    @JsonProperty("thru")
    private Integer thru;

    @JsonProperty("strokes")
    private Integer strokes;

    @JsonProperty("eagles")
    private Integer eagles;

    @JsonProperty("birdies")
    private Integer birdies;

    @JsonProperty("pars")
    private Integer pars;

    @JsonProperty("bogeys")
    private Integer bogeys;

    @JsonProperty("double_bogeys")
    private Integer doubleBogeys;

    @JsonProperty("holes_in_one")
    private Integer holesInOne;

    @JsonProperty("course")
    private SportsRadarTournamentCourseDto course;

    @JsonProperty("scores")
    private List<SportsRadarPlayerScoreDto> scores;

    public SportsRadarTournamentPlayerRoundDto() { super(); }
}
