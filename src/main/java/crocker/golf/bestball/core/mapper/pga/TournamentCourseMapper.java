package crocker.golf.bestball.core.mapper.pga;

import crocker.golf.bestball.domain.enums.pga.EventType;
import crocker.golf.bestball.domain.enums.pga.TournamentState;
import crocker.golf.bestball.domain.pga.tournament.CourseHole;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentCourse;
import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TournamentCourseMapper implements RowMapper<TournamentCourse> {

    @Override
    public TournamentCourse mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return TournamentCourse.builder()
                    .tournamentId((UUID)rs.getObject("TOURNAMENT_ID"))
                    .courseId((UUID)rs.getObject("COURSE_ID"))
                    .courseName(rs.getString("COURSE_NAME"))
                    .yardage(rs.getInt("YARDAGE"))
                    .par(rs.getInt("PAR"))
                    .courseHoles(getCourseHoles(rs))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<CourseHole> getCourseHoles(ResultSet rs) throws SQLException, IOException, ClassNotFoundException {
        return convertByteArrayToList(rs.getBytes("HOLES"));
    }

    private List convertByteArrayToList(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

        return (List)inputStream.readObject();
    }
}
