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
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class DraftController {

    private static final Logger logger = LoggerFactory.getLogger(DraftController.class);

    private DraftService draftService;

    public DraftController(DraftService draftService) {
        this.draftService = draftService;
    }

    @MessageMapping("/loadDraft/{draftId}")
    @SendTo("/ui/refreshDraft/{draftId}")
    public Draft loadDraft(@RequestBody RequestDto requestDto, @DestinationVariable String draftId) {
        logger.info("Received request from {} to load draft {}", requestDto.getEmail(), draftId);
        return draftService.loadDraft(requestDto);
    }

    @MessageMapping("/draftPlayer/{draftId}/{playerId}")
    @SendTo("/ui/refreshDraft/{draftId}")
    public Draft draftPlayer(@RequestBody RequestDto requestDto, @DestinationVariable String draftId, @DestinationVariable String playerId) {
        logger.info("Received request from {} to draft player {} for draft {}", requestDto.getEmail(), playerId, draftId);
        return draftService.draftPlayer(requestDto, UUID.fromString(playerId));
    }

}
