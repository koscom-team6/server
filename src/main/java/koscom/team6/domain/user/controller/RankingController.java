package koscom.team6.domain.user.controller;

import koscom.team6.domain.user.dto.UserRankingResponse;
import koscom.team6.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class RankingController {

    private final UserService userService;

    @GetMapping("/api/ranking")
    public ResponseEntity<?> showRanking() {

        List<UserRankingResponse> users = userService.showRanking();

        return ResponseEntity.ok(users);

    }

}
