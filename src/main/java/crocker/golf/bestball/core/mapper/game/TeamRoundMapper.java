package crocker.golf.bestball.core.mapper.game;

import crocker.golf.bestball.domain.game.round.TeamRound;
import crocker.golf.bestball.domain.pga.tournament.HoleScore;
import crocker.golf.bestball.domain.pga.tournament.PlayerRound;
import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TeamRoundMapper implements RowMapper<TeamRound> {

    @Override
    public TeamRound mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return TeamRound.builder()
                    .teamId((UUID)rs.getObject("TEAM_ID"))
                    .gameId((UUID)rs.getObject("GAME_ID"))
                    .roundId((UUID)rs.getObject("ROUND_ID"))
                    .tournamentId((UUID)rs.getObject("TOURNAMENT_ID"))
                    .roundNumber(rs.getInt("ROUND_NUMBER"))
                    .toPar(rs.getInt("TO_PAR"))
                    .strokes(rs.getInt("STROKES"))
                    .frontNine(rs.getInt("FRONT_NINE"))
                    .backNine(rs.getInt("BACK_NINE"))
                    .holeScores(getHoleScores(rs))
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
