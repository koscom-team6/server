package koscom.team6.domain.match.dto.response;

import koscom.team6.Entity.UserEntity;
import koscom.team6.domain.match.domain.Match;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PracticeResponse {

    private UserEntity user;

    private Match match;

    @Builder
    private PracticeResponse(UserEntity user, UserEntity rival, Match match) {
        this.user = user;
        this.match = match;
    }

    public static PracticeResponse of(UserEntity user, Match match) {
        return PracticeResponse.builder()
                .user(user)
                .match(match)
                .build();
    }
}
