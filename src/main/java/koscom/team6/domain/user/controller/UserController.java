package koscom.team6.domain.user.controller;

import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.user.dto.JoinRequest;
import koscom.team6.domain.user.dto.TokenRequest;
import koscom.team6.domain.user.service.UserService;
import koscom.team6.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.h2.command.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinRequest joinRequest) {

        UserEntity saved = userService.joinProcess(joinRequest);

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/user")
    public ResponseEntity<?> getUser(@RequestBody TokenRequest token) {

        String username = jwtUtil.getUsername(token.getToken());

        userService.getUser(username);

        return ResponseEntity.ok(username);
    }
}
