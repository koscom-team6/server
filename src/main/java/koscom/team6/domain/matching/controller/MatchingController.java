package koscom.team6.domain.matching.controller;

import koscom.team6.domain.matching.dto.*;
import koscom.team6.domain.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MatchingController {

    private final MatchingService matchingService;

    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public AnswerResponse sendMsg(AnswerRequest request) {

        return new AnswerResponse(request.getPlayerId(), request.getContent());

    }

    @PostMapping("/matching")
    public ResponseEntity<?> requestMatching(@RequestBody MatchingUserRequest request) {

        matchingService.requestMatching(request.getPlayerId());

        return ResponseEntity.ok(request.getPlayerId());
    }
/*
    @MessageMapping("/typing")
    @SendTo("/sub/typing")
    public TypingResponseStatus handleTypingStatus(TypingRequestStatus status) {

        TypingResponseStatus responseStatus = new TypingResponseStatus(status.getPlayerId(), true);

        return responseStatus;
    }*/

    @PostMapping("/arena")
    public ResponseEntity<ArenaObject> sendMessage(ArenaRequest request) {

        ArenaObject arenaObject = matchingService.getMessage(request);

        return ResponseEntity.ok(arenaObject);
    }


    @MessageMapping("/matching")
    @SendTo("/sub/matching")
    public MatchingResponse startMatching(MatchingStartRequest request) {

        return new MatchingResponse(request.getPlayer1Id(), request.getPlayer2Id(), "" );
    }

}