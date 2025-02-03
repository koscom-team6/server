package koscom.team6.domain.matching.dto;

import koscom.team6.domain.match.Entity.Match;
import koscom.team6.domain.match.vo.MatchResultVO;
import koscom.team6.domain.user.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaObject {

    private String type;
    private String matchSessionId;

    private UserEntity user1;
    private UserEntity user2;

    private Long problemId;

    private String content1;
    private String content2;

    private MatchResultVO result1;
    private MatchResultVO result2;

    private boolean solved1;
    private boolean solved2;

}
