package koscom.team6.service;

import koscom.team6.Entity.UserEntity;
import koscom.team6.dto.JoinRequest;
import koscom.team6.dto.UserRankingResponse;
import koscom.team6.repository.UserRepository;
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
}
