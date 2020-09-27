package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.game.InfoService;
import crocker.golf.bestball.domain.game.TeamInfo;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    private InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @PostMapping("/getTeamInfo")
    public ResponseEntity getTeamInfo(@RequestBody UserCredentialsDto userCredentialsDto ) {
        try {
            logger.info("Received request to obtain team info from {}", userCredentialsDto.getEmail());
            List<TeamInfo> teamInfo = infoService.getTeamInfo(userCredentialsDto);
            return new ResponseEntity<>(teamInfo, null, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
