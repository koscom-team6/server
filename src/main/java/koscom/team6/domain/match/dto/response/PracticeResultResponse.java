package koscom.team6.domain.match.dto.response;

import koscom.team6.Entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PracticeResultResponse {

    UserEntity user;

    String userAnswer;

    String AIAnswer;

    Integer userScore;

    @Builder
    private PracticeResultResponse(UserEntity user, String userAnswer, String AIAnswer, Integer userScore) {
        this.user = user;
        this.userAnswer = userAnswer;
        this.AIAnswer = AIAnswer;
        this.userScore = userScore;
    }

    public static PracticeResultResponse of(UserEntity user, String userAnswer, String AIAnswer, Integer userScore) {
        return PracticeResultResponse.builder()
                .user(user)
                .userAnswer(userAnswer)
                .AIAnswer(AIAnswer)
                .userScore(userScore)
                .build();
    }

}
