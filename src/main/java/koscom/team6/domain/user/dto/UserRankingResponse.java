package koscom.team6.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRankingResponse {

    String nickname;
    int score;
    int solvedCount;
    int winningCount;
    String imageUrl;
    int totalCount;

}
