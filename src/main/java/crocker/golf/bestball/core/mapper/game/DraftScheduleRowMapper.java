package crocker.golf.bestball.core.mapper.game;

import crocker.golf.bestball.domain.enums.game.DraftState;
import crocker.golf.bestball.domain.enums.game.ReleaseStatus;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DraftScheduleRowMapper implements RowMapper<DraftSchedule> {

    @Override
    public DraftSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DraftSchedule.builder()
                .draftId((UUID)rs.getObject("DRAFT_ID"))
                .releaseTime(rs.getTimestamp("RELEASE_TIME").toLocalDateTime())
                .releaseStatus(ReleaseStatus.valueOf(rs.getString("RELEASE_STATUS")))
                .build();
    }
}
