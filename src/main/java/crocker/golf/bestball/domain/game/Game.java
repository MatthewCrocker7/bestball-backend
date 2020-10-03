package crocker.golf.bestball.domain.game;

import crocker.golf.bestball.domain.enums.game.GameState;
import crocker.golf.bestball.domain.enums.game.GameType;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import crocker.golf.bestball.domain.pga.tournament.TournamentSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Game {

    private UUID gameId;
    private GameState gameState;
    private Integer gameVersion;
    private GameType gameType;

    private UUID draftId;
    private Tournament tournament;
    private TournamentSummary tournamentSummary;

    private Integer numPlayers;
    private BigDecimal buyIn;
    private BigDecimal moneyPot;

    private List<Team> teams;
}
