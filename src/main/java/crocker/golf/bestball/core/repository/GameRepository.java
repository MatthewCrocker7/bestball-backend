package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.GameDao;
import crocker.golf.bestball.core.dao.TeamDao;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.Team;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.UUID;

public class GameRepository {

    private GameDao gameDao;
    private TeamDao teamDao;

    public GameRepository(GameDao gameDao, TeamDao teamDao) {
        this.gameDao = gameDao;
        this.teamDao = teamDao;
    }

    public void saveNewGame(Game game) {
        gameDao.saveNewGame(game);
    }

    @CacheEvict(value = "teamsByUserId", key = "#team.getUserId()")
    public void saveNewTeam(Team team) { teamDao.saveTeam(team); }

    public Game getLatestGameById(UUID gameId) { return gameDao.getLatestGameById(gameId); }

    @Cacheable(value = "teamsByUserId", key = "#userId")
    public List<Team> getTeamsByUserId(UUID userId) {
        return teamDao.getTeamsByUserId(userId);
    }

}
