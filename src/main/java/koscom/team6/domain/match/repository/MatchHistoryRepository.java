package koscom.team6.domain.match.repository;

import koscom.team6.domain.match.Entity.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {
}
