package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.Tournament;

import java.util.List;
import java.util.UUID;

public interface PgaDao {

    void updateWorldRankings(List<PgaPlayer> pgaPlayers);

    void updateSeasonSchedule(List<Tournament> tournaments);

    List<PgaPlayer> getWorldRankings();

    List<Tournament> getTournamentsBySeason(int year);

    Tournament getTournamentById(UUID tournamentId);
}
