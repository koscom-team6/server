package koscom.team6.domain.match.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchingSaveResponse {

    private long id;

    private String title;

    private String content;

    private String imageUrl1;

    private String imageDes1;

    private String imageUrl2;

    private String imageDes2;

    private String imageUrl3;

    private String imageDes3;

    private String imageUrl4;

    private String imageDes4;

    private String tag1;

    private String tag2;

    private String tag3;

    private String referText1;

    private String referLink1;

    private String referText2;

    private String referLink2;

    private String referText3;

    private String referLink3;

}
