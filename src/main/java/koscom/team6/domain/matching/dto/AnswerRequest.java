package koscom.team6.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerRequest {

    int playerId;
    String content;

}