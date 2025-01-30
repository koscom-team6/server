package koscom.team6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchStartRequest {

    int player1Id;
    int player2Id;

}
