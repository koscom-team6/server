package koscom.team6.domain.match.repository;

import koscom.team6.domain.match.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
