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
public class MatchAnswer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MatchHistory matchHistory;

    @ManyToOne
    private UserEntity user;

    @Column
    private String userAnswer;

    @Column
    private String AIAnswer;

    @Builder
    private MatchAnswer(MatchHistory matchHistory, UserEntity user, String userAnswer, String AIAnswer) {
        this.matchHistory = matchHistory;
        this.user = user;
        this.userAnswer = userAnswer;
        this.AIAnswer = AIAnswer;
    }

    public static MatchAnswer of(MatchHistory matchHistory, UserEntity user, String userAnswer, String AIAnswer) {
        return MatchAnswer.builder()
                .matchHistory(matchHistory)
                .user(user)
                .userAnswer(userAnswer)
                .AIAnswer(AIAnswer)
                .build();
    }

}
