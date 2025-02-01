package koscom.team6.domain.match.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchResultRequest {

    private Long matchId;

    private Long rivalId;

    private String userAnswer;

    private String rivalAnswer;

    @Builder
    private MatchResultRequest(Long matchId, Long rivalId, String userAnswer, String rivalAnswer) {
        this.matchId = matchId;
        this.rivalId = rivalId;
        this.userAnswer = userAnswer;
        this.rivalAnswer = rivalAnswer;
    }

}
