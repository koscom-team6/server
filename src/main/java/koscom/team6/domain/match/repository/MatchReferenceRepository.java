package koscom.team6.domain.match.repository;

import koscom.team6.domain.match.Entity.Matching;
import koscom.team6.domain.match.Entity.MatchReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchReferenceRepository extends JpaRepository<MatchReference, Long> {
    List<MatchReference> findAllByMatching(Matching matching);
}
