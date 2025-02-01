package koscom.team6.domain.match.dto.response;

import koscom.team6.Entity.UserEntity;
import koscom.team6.domain.match.vo.MatchResultVO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchResultResponse {

    UserEntity user;

    MatchResultVO userResult;

    MatchResultVO rivalResult;

    @Builder
    private MatchResultResponse(UserEntity user, MatchResultVO userResult, MatchResultVO rivalResult) {
        this.user = user;
        this.userResult = userResult;
        this.rivalResult = rivalResult;
    }

    public static MatchResultResponse of(UserEntity user, MatchResultVO userResult, MatchResultVO rivalResult) {
        return MatchResultResponse.builder()
                .user(user)
                .userResult(userResult)
                .rivalResult(rivalResult)
                .build();
    }

}
