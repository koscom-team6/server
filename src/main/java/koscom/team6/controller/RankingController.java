package koscom.team6.controller;

import koscom.team6.dto.UserRankingResponse;
import koscom.team6.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
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
