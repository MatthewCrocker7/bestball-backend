package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.pga.PgaInfoService;
import crocker.golf.bestball.core.service.pga.PgaUpdateService;
import crocker.golf.bestball.domain.pga.tournament.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pga")
public class PgaController {

    private PgaInfoService pgaInfoService;

    private static final Logger logger = LoggerFactory.getLogger(PgaController.class);

    public PgaController(PgaInfoService pgaInfoService) {
        this.pgaInfoService = pgaInfoService;
    }

    @GetMapping("/upcomingTournaments")
    public ResponseEntity getUpcomingTournaments() {
        List<Tournament> tournaments = pgaInfoService.getUpcomingTournaments();

        return new ResponseEntity<>(tournaments, null, HttpStatus.OK);
    }
}
