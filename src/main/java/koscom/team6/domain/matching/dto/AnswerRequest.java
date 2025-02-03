package koscom.team6.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerRequest {

    int playerId;

    String content;

    boolean player1answer = false;

    boolean player2answer = false;

}