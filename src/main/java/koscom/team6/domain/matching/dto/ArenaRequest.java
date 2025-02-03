package koscom.team6.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArenaRequest {

    private String matchSessionId;
    private String type;
    private String content;
    private String token;

}
