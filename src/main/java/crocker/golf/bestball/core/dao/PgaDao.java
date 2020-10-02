package crocker.golf.bestball.core.dao;

import crocker.golf.bestball.domain.pga.PgaPlayer;
import crocker.golf.bestball.domain.pga.tournament.*;

import java.util.List;
import java.util.UUID;

public interface PgaDao {

    void updateWorldRankings(List<PgaPlayer> pgaPlayers);

    List<PgaPlayer> getWorldRankings();

    void updateSeasonSchedule(List<Tournament> tournaments);

    List<Tournament> getTournamentsBySeason(int year);

    Tournament getTournamentById(UUID tournamentId);

    List<Tournament> getAllTournaments();

    void updateTournamentSummary(TournamentSummary tournamentSummary);

    List<TournamentCourse> getTournamentCourses(UUID tournamentId);

    List<TournamentRound> getTournamentRounds(UUID tournamentId);

    void updatePlayerRounds(List<PlayerRound> playerRounds);

    List<PlayerRound> getPlayerRoundsByTournamentId(UUID tournamentId);
}
