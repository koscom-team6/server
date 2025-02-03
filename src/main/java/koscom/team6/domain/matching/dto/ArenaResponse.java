package koscom.team6.domain.matching.dto;

import koscom.team6.domain.match.vo.MatchResultVO;
import koscom.team6.domain.user.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaResponse {

    private String matchSessionId;
    private String type;
    private String content;
    private String nickname;

    private UserEntity user;
    private UserEntity rivalUser;

    private int problemId;

    private boolean solved = false;
    private boolean rivalSolved = false;

    private MatchResultVO aiResult;
    private MatchResultVO rivalAiResult;

}
