package koscom.team6.domain.match.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PracticeResultRequest {

    private Long matchId;

    private String userAnswer;

    @Builder
    private PracticeResultRequest(Long matchId, String userAnswer) {
        this.matchId = matchId;
        this.userAnswer = userAnswer;
    }

}
