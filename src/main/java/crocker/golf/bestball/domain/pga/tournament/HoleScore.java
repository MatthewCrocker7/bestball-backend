package crocker.golf.bestball.domain.pga.tournament;

import crocker.golf.bestball.domain.enums.game.ScoreType;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class HoleScore implements Serializable {

    private Integer holeNumber;
    private Integer par;
    private Integer yardage;
    private Integer strokes;
    private ScoreType scoreType;
}
