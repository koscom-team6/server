package koscom.team6.domain.user.repository;

import koscom.team6.domain.user.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    Page<UserEntity> findAllByOrderByScoreDescWinningCountAsc(Pageable pageable);

    UserEntity findById(Long rivalId);
}
