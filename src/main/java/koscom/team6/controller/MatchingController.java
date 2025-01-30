package koscom.team6.controller;

import koscom.team6.dto.*;
import koscom.team6.service.MatchService;
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

    private final MatchService matchService;

    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public AnswerResponse sendMsg(AnswerRequest request) {

        return new AnswerResponse(request.getPlayerId(), request.getContent());

    }

    @PostMapping("/matching")
    public ResponseEntity<?> requestMatching(@RequestBody MatchUserRequest request) {

        matchService.requestMatching(request.getPlayerId());

        return ResponseEntity.ok(request.getPlayerId());
    }

    @MessageMapping("/matching")
    @SendTo("/sub/matching")
    public MatchResponse startMatching(MatchStartRequest request) {

        return new MatchResponse(request.getPlayer1Id(), request.getPlayer2Id());
    }

}
