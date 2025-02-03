package koscom.team6.domain.match.dto.response;

import koscom.team6.domain.match.Entity.Matching;
import koscom.team6.domain.user.Entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PracticeResponse {

    private UserEntity user;

    private Matching matching;

    @Builder
    private PracticeResponse(UserEntity user, UserEntity rival, Matching matching) {
        this.user = user;
        this.matching = matching;
    }

    public static PracticeResponse of(UserEntity user, Matching matching) {
        return PracticeResponse.builder()
                .user(user)
                .matching(matching)
                .build();
    }
}
