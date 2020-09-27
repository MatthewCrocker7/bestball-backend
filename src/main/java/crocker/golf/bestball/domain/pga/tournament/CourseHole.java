package crocker.golf.bestball.domain.pga.tournament;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CourseHole implements Serializable {

    private Integer holeNumber;
    private Integer par;
    private Integer yardage;
}
