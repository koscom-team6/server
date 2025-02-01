package koscom.team6.domain.match.repository;

import koscom.team6.domain.match.Entity.MatchAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchAnswerRepository extends JpaRepository<MatchAnswer, Long> {
}
