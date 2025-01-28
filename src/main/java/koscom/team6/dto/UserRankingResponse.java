package koscom.team6.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRankingResponse {

    String nickname;
    int score;
    int solvedCount;

}
