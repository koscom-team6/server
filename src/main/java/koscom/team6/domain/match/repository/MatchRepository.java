package koscom.team6.domain.match.repository;

import koscom.team6.domain.match.Entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Matching, Long> {
}
