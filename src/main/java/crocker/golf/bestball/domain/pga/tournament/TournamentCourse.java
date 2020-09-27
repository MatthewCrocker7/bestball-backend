package crocker.golf.bestball.domain.pga.tournament;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class TournamentCourse {

    private UUID tournamentId;
    private UUID courseId;
    private String courseName;
    private Integer yardage;
    private Integer par;
    List<CourseHole> courseHoles;

}
