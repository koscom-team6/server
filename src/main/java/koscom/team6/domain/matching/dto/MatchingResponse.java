package koscom.team6.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingResponse {

    Long player1Id;
    Long player2Id;
    String matchSessionId;
}