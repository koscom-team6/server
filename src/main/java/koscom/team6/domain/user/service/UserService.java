package koscom.team6.domain.user.service;

import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.user.dto.JoinRequest;
import koscom.team6.domain.user.dto.UserRankingResponse;
import koscom.team6.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserRankingResponse> showRanking() {

        List<UserEntity> users = userRepository.findAllByOrderByScoreDescSolvedCountAsc();

        return users.stream()
                .map(userEntity -> {
                    UserRankingResponse response = new UserRankingResponse();
                    response.setNickname(userEntity.getNickname());
                    response.setScore(userEntity.getScore());
                    response.setSolvedCount(userEntity.getSolvedCount());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public UserEntity getUser(String username) {
        UserEntity user = userRepository.findByUsername(username);

        return user;
    }
}
