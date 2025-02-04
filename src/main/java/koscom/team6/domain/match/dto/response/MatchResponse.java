package koscom.team6.domain.match.dto.response;

import koscom.team6.domain.match.Entity.Matching;
import koscom.team6.domain.match.Entity.MatchReference;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MatchResponse {

    private Long matchId;

    private String matchTitle;

    private String matchContent;

    private List<String> images;

    private List<String> tags;

    private List<MatchReferenceResponse> matchReferences;

    @Builder
    private MatchResponse(Long matchId, String matchTitle, String matchContent, List<String> images, List<String> tags, List<MatchReferenceResponse> matchReferences) {
        this.matchId = matchId;
        this.matchTitle = matchTitle;
        this.matchContent = matchContent;
        this.images = images;
        this.tags = tags;
        this.matchReferences = matchReferences;
    }

    public static MatchResponse of(Matching matching, List<MatchReferenceResponse> matchReferences) {
        return MatchResponse.builder()
                .matchId(matching.getId())
                .matchTitle(matching.getTitle())
                .matchContent(matching.getContent())
                .images(List.of(matching.getImageUrl1(), matching.getImageUrl2(), matching.getImageUrl3()))
                .tags(List.of(matching.getTag1(), matching.getTag2(), matching.getTag3()))
                .matchReferences(matchReferences)
                .build();
    }
}
