package crocker.golf.bestball.core.controllers;

import crocker.golf.bestball.core.service.game.DraftService;
import crocker.golf.bestball.domain.game.draft.Draft;
import crocker.golf.bestball.domain.user.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class DraftController {

    private static final Logger logger = LoggerFactory.getLogger(DraftController.class);

    private DraftService draftService;

    public DraftController(DraftService draftService) {
        this.draftService = draftService;

       // forceDraft();
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


    private void forceDraft()
    {
        RequestDto requestDto = new RequestDto();
        requestDto.setDraftId("222bd386-333d-4d7c-8f9c-dbe509977401");
        requestDto.setGameId("c06b88ca-f0ff-4157-a7e3-80fb8bd6f1cb");
        requestDto.setEmail("matthewcroc@gmail.com");
        requestDto.setInviteEmail("matthewcroc@gmail.com");

        draftService.draftPlayer(requestDto, UUID.fromString("24b500eb-eee7-4bed-a06f-c76296c076f5"));
    }



}
