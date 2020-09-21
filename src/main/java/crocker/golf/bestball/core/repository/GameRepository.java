package crocker.golf.bestball.core.repository;

import crocker.golf.bestball.core.dao.GameDao;
import crocker.golf.bestball.core.dao.TeamDao;
import crocker.golf.bestball.domain.game.Game;
import crocker.golf.bestball.domain.game.Team;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

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

    @Caching(evict = {
        @CacheEvict(value = "teamsByUserId", key = "#team.getUserId()"),
        @CacheEvict(value = "teamsByDraftId", key = "#team.getDraftId()")
    })
    public void saveNewTeam(Team team) { teamDao.saveTeam(team); }

    @Caching(evict = {
            @CacheEvict(value = "teamsByUserId", key = "#team.getUserId()"),
            @CacheEvict(value = "teamsByDraftId", key = "#team.getDraftId()")
    })
    public void updateTeam(Team team) { teamDao.updateTeam(team); }

    @Cacheable(value = "gameByGameId", key = "#gameId")
    public Game getLatestGameByGameId(UUID gameId) { return gameDao.getLatestGameByGameId(gameId); }

    @Cacheable(value = "gameByDraftId", key = "#draftId")
    public Game getLatestGameByDraftId(UUID draftId) { return gameDao.getLatestGameByDraftId(draftId); }

    @Cacheable(value = "teamsByUserId", key = "#userId")
    public List<Team> getTeamsByUserId(UUID userId) {
        return teamDao.getTeamsByUserId(userId);
    }

    @Cacheable(value = "teamsByDraftId", key = "#draftId")
    public List<Team> getTeamsByDraftId(UUID draftId) {
        return teamDao.getTeamsByDraftId(draftId);
    }

    public Team getTeamByUserAndDraftId(UUID userId, UUID draftId) {
        return teamDao.getTeamByUserAndDraftId(userId, draftId);
    }

    public Team getTeamByUserAndGameId(UUID userId, UUID gameId) {
        return teamDao.getTeamByUserAndGameId(userId, gameId);
    }
}
