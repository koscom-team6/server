package koscom.team6.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingStartRequest {

    int player1Id;
    int player2Id;

}