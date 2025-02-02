package koscom.team6.domain.user.controller;

import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.user.dto.JoinRequest;
import koscom.team6.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinRequest joinRequest) {

//        System.out.println(joinRequest.getUsername());
        UserEntity saved = userService.joinProcess(joinRequest);

        return ResponseEntity.ok(saved);
    }

}
