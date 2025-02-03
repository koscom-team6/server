package koscom.team6.domain.match.dto.response;

import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.match.vo.MatchResultVO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchResultResponse {

    UserEntity user;

    MatchResultVO userResult;

    MatchResultVO rivalResult;

    int addScore;

    @Builder
    private MatchResultResponse(UserEntity user, MatchResultVO userResult, MatchResultVO rivalResult, int addScore) {
        this.user = user;
        this.userResult = userResult;
        this.rivalResult = rivalResult;
        this.addScore = addScore;
    }

    public static MatchResultResponse of(UserEntity user, MatchResultVO userResult, MatchResultVO rivalResult, int addScore) {
        return MatchResultResponse.builder()
                .user(user)
                .userResult(userResult)
                .rivalResult(rivalResult)
                .addScore(addScore)
                .build();
    }

}
