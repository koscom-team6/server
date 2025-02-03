package koscom.team6.domain.match.dto.response;

import koscom.team6.domain.match.Entity.Match;
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

    private List<MatchReference> matchReferences;

    @Builder
    private MatchResponse(Long matchId, String matchTitle, String matchContent, List<String> images, List<String> tags, List<MatchReference> matchReferences) {
        this.matchId = matchId;
        this.matchTitle = matchTitle;
        this.matchContent = matchContent;
        this.images = images;
        this.tags = tags;
        this.matchReferences = matchReferences;
    }

    public static MatchResponse of(Match match, List<MatchReference> matchReferences) {
        return MatchResponse.builder()
                .matchId(match.getId())
                .matchTitle(match.getTitle())
                .matchContent(match.getContent())
                .images(List.of(match.getImageUrl1(), match.getImageUrl2(), match.getImageUrl3(), match.getImageUrl4()))
                .tags(List.of(match.getTag1(), match.getTag2(), match.getTag3()))
                .matchReferences(matchReferences)
                .build();
    }
}
