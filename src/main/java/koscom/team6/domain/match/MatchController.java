package koscom.team6.domain.match;

import koscom.team6.domain.match.dto.request.MatchResultRequest;
import koscom.team6.domain.match.dto.request.PracticeResultRequest;
import koscom.team6.domain.match.dto.response.MatchResponse;
import koscom.team6.domain.match.dto.response.MatchResultResponse;
import koscom.team6.domain.match.dto.response.PracticeResponse;
import koscom.team6.domain.match.dto.response.PracticeResultResponse;
import koscom.team6.domain.match.service.MatchService;
import koscom.team6.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/match") //?
public class MatchController {

    private final MatchService matchService;

    @GetMapping("practice")
    // 연습게임
    public ResponseEntity<PracticeResponse> getPractice(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PracticeResponse practiceResponse = matchService.getPractice(userDetails);
        return ResponseEntity.ok(practiceResponse);
    }

    @PostMapping("practice/result")
    public ResponseEntity<PracticeResultResponse> getPracticeResult(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PracticeResultRequest practiceResultRequest
    ) {
        PracticeResultResponse practiceResultResponse = matchService.getPracticeResult(userDetails, practiceResultRequest);
        return ResponseEntity.ok(practiceResultResponse);
    }

    @GetMapping("")
    public ResponseEntity<MatchResponse> getMatch(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long rivalId,
            @RequestParam Long roomId
    ) {
        MatchResponse matchResponse = matchService.getMatch(userDetails, rivalId, roomId);
        return ResponseEntity.ok(matchResponse);
    }

    @PostMapping("result")
    public ResponseEntity<MatchResultResponse> getResult(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchResultRequest matchResultRequest
    ) {
        MatchResultResponse matchResultResponse = matchService.getMatchResult(userDetails, matchResultRequest);
        return ResponseEntity.ok(matchResultResponse);
    }
}
