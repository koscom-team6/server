package koscom.team6.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {

    private String username;
    private String password;
    private String nickname;

}
