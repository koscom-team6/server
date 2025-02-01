package koscom.team6.domain.match.dto.response;

import koscom.team6.domain.match.Entity.Match;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchResponse {

    private Match match;

    @Builder
    private MatchResponse(Match match) {
        this.match = match;
    }

    public static MatchResponse from(Match match) {
        return MatchResponse.builder()
                .match(match)
                .build();
    }
}
