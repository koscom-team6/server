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
    private Matching matching;

    @ManyToOne
    private UserEntity userA;

    @ManyToOne
    private UserEntity userB;

    @Builder
    private MatchHistory(Matching matching, UserEntity userA, UserEntity userB) {
        this.matching = matching;
        this.userA = userA;
        this.userB = userB;
    }

    public static MatchHistory of(Matching matching, UserEntity userA, UserEntity userB) {
        return MatchHistory.builder()
                .matching(matching)
                .userA(userA)
                .userB(userB)
                .build();
    }

}
