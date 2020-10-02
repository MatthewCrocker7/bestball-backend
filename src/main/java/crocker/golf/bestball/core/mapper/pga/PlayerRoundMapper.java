package crocker.golf.bestball.core.mapper.pga;

import crocker.golf.bestball.domain.enums.pga.Status;
import crocker.golf.bestball.domain.pga.tournament.CourseHole;
import crocker.golf.bestball.domain.pga.tournament.HoleScore;
import crocker.golf.bestball.domain.pga.tournament.PlayerRound;
import crocker.golf.bestball.domain.pga.tournament.TournamentRound;
import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayerRoundMapper implements RowMapper<PlayerRound> {

    @Override
    public PlayerRound mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return PlayerRound.builder()
                    .playerId((UUID)rs.getObject("PLAYER_ID"))
                    .tournamentId((UUID)rs.getObject("TOURNAMENT_ID"))
                    .roundId((UUID)rs.getObject("ROUND_ID"))
                    .roundNumber(rs.getInt("ROUND_NUMBER"))
                    .courseId((UUID)rs.getObject("COURSE_ID"))
                    .toPar(rs.getInt("TO_PAR"))
                    .thru(rs.getInt("THRU"))
                    .strokes(rs.getInt("STROKES"))
                    .scores(getHoleScores(rs))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<HoleScore> getHoleScores(ResultSet rs) throws SQLException, IOException, ClassNotFoundException {
        return convertByteArrayToList(rs.getBytes("SCORES"));
    }

    private List convertByteArrayToList(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

        return (List)inputStream.readObject();
    }
}
