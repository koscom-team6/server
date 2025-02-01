package koscom.team6.domain.match.Entity;

import jakarta.persistence.*;
import koscom.team6.domain.user.Entity.UserEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Match match;

    @ManyToOne
    private UserEntity userA;

    @ManyToOne
    private UserEntity userB;

    @Builder
    private MatchHistory(Match match, UserEntity userA, UserEntity userB) {
        this.match = match;
        this.userA = userA;
        this.userB = userB;
    }

    public static MatchHistory of(Match match, UserEntity userA, UserEntity userB) {
        return MatchHistory.builder()
                .match(match)
                .userA(userA)
                .userB(userB)
                .build();
    }

}
