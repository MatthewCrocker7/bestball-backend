package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.game.DraftService;
import crocker.golf.bestball.core.service.game.GameService;
import crocker.golf.bestball.domain.game.GameDto;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/draft")
public class DraftController {

    private static final Logger logger = LoggerFactory.getLogger(DraftController.class);

    private DraftService draftService;

    public DraftController(DraftService draftService) {
        this.draftService = draftService;
    }

    @PostMapping("/loadDraft")
    public ResponseEntity loadDraft(@RequestBody RequestDto requestDto) {
        try {
            logger.info("Received request from {} to load draft {}", requestDto.getEmail(), requestDto.getDraftId());
            Draft draft = draftService.loadDraft(requestDto);
            return new ResponseEntity(draft, null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
