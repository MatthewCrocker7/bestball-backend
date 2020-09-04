package crocker.golf.bestball.core.mapper;

import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.game.draft.Draft;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DraftRowMapper implements RowMapper<Draft> {

    @Override
    public Draft mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Draft.builder()
                .draftId((UUID)rs.getObject("DRAFT_ID"))
                .draftState(DraftState.valueOf(rs.getString("DRAFT_STATE")))
                .draftVersion(rs.getInt("DRAFT_VERSION"))
                .startTime(rs.getTimestamp("DRAFT_TIME").toLocalDateTime())
                .currentPick(rs.getInt("CURRENT_PICK"))
                .build();
    }
}
