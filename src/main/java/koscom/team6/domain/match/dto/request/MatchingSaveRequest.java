package koscom.team6.domain.match.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchingSaveRequest {

    private String title;

    private String content;

    private String imageUrl1;

    private String imageDescription1;

    private String imageUrl2;

    private String imageDescription2;

    private String imageUrl3;

    private String imageDescription3;

    private String imageUrl4;

    private String imageDescription4;

    private String tag1;

    private String tag2;

    private String tag3;

    private String reference1;

    private String referenceLink1;

    private String reference2;

    private String referenceLink2;

    private String reference3;

    private String referenceLink3;

}
