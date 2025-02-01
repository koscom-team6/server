package koscom.team6.domain.user.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private int score;

    private int solvedCount;

    private int winningCount;

    private String image;

    public void setWinner(int score) {
        this.score += score;
        this.solvedCount += 1;
        this.winningCount += 1;
    }

    public void setLoser() {
        this.solvedCount += 1;
    }

}
