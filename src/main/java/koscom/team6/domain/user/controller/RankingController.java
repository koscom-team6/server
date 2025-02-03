package koscom.team6.domain.user.controller;

import koscom.team6.domain.user.dto.UserRankingResponse;
import koscom.team6.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ResponseBody
@RequiredArgsConstructor
public class RankingController {

    private final UserService userService;

    @GetMapping("/api/ranking")
    public ResponseEntity<?> showRanking(Pageable pageable) {

        Page<UserRankingResponse> users = userService.showRanking(pageable);

        return ResponseEntity.ok(users);

    }

}
