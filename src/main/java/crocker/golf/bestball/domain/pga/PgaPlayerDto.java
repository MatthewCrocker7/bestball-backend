package crocker.golf.bestball.domain.pga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PgaPlayerDto {

    @JsonProperty("PlayerSeasonID")
    private Integer playerSeasonID;

    @JsonProperty("Season")
    private Integer season;

    @JsonProperty("PlayerID")
    private Integer playerID;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("WorldGolfRank")
    private Integer worldGolfRank;

    @JsonProperty("WorldGolfRankLastWeek")
    private Integer worldGolfRankLastWeek;

    @JsonProperty("Events")
    private Integer events;

    @JsonProperty("AveragePoints")
    private Double averagePoints;

    @JsonProperty("TotalPoints")
    private Double totalPoints;

    @JsonProperty("PointsLost")
    private Double pointsLost;

    @JsonProperty("PointsGained")
    private Double pointsGained;

    public PgaPlayerDto() {
        super();
    }
}
