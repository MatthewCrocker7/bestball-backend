package crocker.golf.bestball.domain.pga.sports.radar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportsRadarTournamentCourseDto {

    @JsonProperty("id")
    private String courseId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("yardage")
    private Integer yardage;

    @JsonProperty("par")
    private Integer par;

    @JsonProperty("holes")
    private List<SportsRadarCourseHoleDto> holes;

    public SportsRadarTournamentCourseDto() { super(); }
}
