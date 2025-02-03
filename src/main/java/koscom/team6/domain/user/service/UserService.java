package koscom.team6.domain.user.service;

import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.user.dto.JoinRequest;
import koscom.team6.domain.user.dto.UserRankingResponse;
import koscom.team6.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity joinProcess(JoinRequest joinRequest) {

        String username = joinRequest.getUsername();
        String password = joinRequest.getPassword();
        String nickname = joinRequest.getNickname();
        String imageUrl = joinRequest.getImageUrl();

/*        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {

            return;
        }*/

        UserEntity user = new UserEntity();

        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setNickname(nickname);
        user.setScore(0);
        user.setSolvedCount(0);
        user.setImage(imageUrl);

        UserEntity saved = userRepository.save(user);

        return saved;
    }

    public Page<UserRankingResponse> showRanking(Pageable pageable) {

        Page<UserEntity> users = userRepository.findAllByOrderByScoreDescWinningCountAsc(pageable);

        return users.map(userEntity -> {
            UserRankingResponse response = new UserRankingResponse();
            response.setNickname(userEntity.getNickname());
            response.setScore(userEntity.getScore());
            response.setSolvedCount(userEntity.getSolvedCount());
            response.setWinningCount(userEntity.getWinningCount());
            response.setImageUrl(userEntity.getImage());
            response.setTotalCount(userRepository.findAll().size());
            return response;
        });
    }

    public UserEntity getUser(String username) {
        UserEntity user = userRepository.findByUsername(username);

        return user;
    }
}
