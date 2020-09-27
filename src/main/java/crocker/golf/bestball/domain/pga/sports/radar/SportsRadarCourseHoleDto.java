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
public class SportsRadarCourseHoleDto {

    @JsonProperty("number")
    private Integer holeNumber;

    @JsonProperty("par")
    private Integer par;

    @JsonProperty("yardage")
    private Integer yardage;

    public SportsRadarCourseHoleDto() { super(); }
}
