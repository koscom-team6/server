package koscom.team6.domain.matching.controller;

import koscom.team6.domain.matching.dto.*;
import koscom.team6.domain.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public ResponseEntity<AnswerResponse> sendMsg(AnswerRequest request) {

        AnswerResponse answerResponse = new AnswerResponse(request.getPlayerId(), request.getContent(), true);

        return ResponseEntity.ok(answerResponse);
    }

    @MessageMapping("/typing")
    @SendTo("/sub/typing")
    public ResponseEntity<TypingResponseStatus> handleTypingStatus(TypingRequestStatus status) {

        TypingResponseStatus typingResponseStatus = new TypingResponseStatus(status.getPlayerId(), true);

        return ResponseEntity.ok(typingResponseStatus);
    }

    @PostMapping("/matching")
    public ResponseEntity<?> requestMatching(@RequestBody MatchingUserRequest request) {

        matchingService.requestMatching(request.getPlayerId());

        return ResponseEntity.ok(request.getPlayerId());
    }

    @MessageMapping("/matching")
    @SendTo("/sub/matching")
    public ResponseEntity<MatchingResponse> startMatching(MatchingStartRequest request) {

        MatchingResponse matchingResponse = new MatchingResponse(request.getPlayer1Id(), request.getPlayer2Id());

        return ResponseEntity.ok(matchingResponse);
    }

}