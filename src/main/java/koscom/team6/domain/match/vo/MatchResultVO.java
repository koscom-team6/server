package koscom.team6.domain.match.vo;

import koscom.team6.domain.match.domain.MatchAnswer;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchResultVO {

    String userAnswer;

    String AIAnswer;

    int score;

    @Builder
    private MatchResultVO(String userAnswer, String AIAnswer, int score) {
        this.userAnswer = userAnswer;
        this.AIAnswer = AIAnswer;
        this.score = score;
    }

    public static MatchResultVO of(MatchAnswer matchAnswer, int score) {
        return MatchResultVO.builder()
                .userAnswer(matchAnswer.getUserAnswer())
                .AIAnswer(matchAnswer.getAIAnswer())
                .score(score)
                .build();
    }

}
