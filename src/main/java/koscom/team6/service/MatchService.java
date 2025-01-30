package koscom.team6.service;

import jakarta.transaction.Transactional;
import koscom.team6.Entity.UserEntity;
import koscom.team6.dto.JoinRequest;
import koscom.team6.dto.MatchResponse;
import koscom.team6.dto.MatchStartRequest;
import koscom.team6.dto.UserRankingResponse;
import koscom.team6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String QUEUE_KEY = "matchingQueue";

    @Transactional
    public void requestMatching(int playerId) {

        // Redis 큐에 플레이어 추가
        redisTemplate.opsForList().rightPush(QUEUE_KEY, playerId);

        // 큐의 크기 확인
        Long queueSize = redisTemplate.opsForList().size(QUEUE_KEY);

        // 두 명의 플레이어가 매칭되었는지 확인
        if (queueSize != null && queueSize >= 2) {

            // 두 명의 플레이어를 큐에서 꺼냄
            Integer player1Id = redisTemplate.opsForList().leftPop(QUEUE_KEY);
            Integer player2Id = redisTemplate.opsForList().leftPop(QUEUE_KEY);

            System.out.println("player1Id = " + player1Id);
            System.out.println("player2Id = " + player2Id);

            if (player1Id != null && player2Id != null) {

                // 매칭 결과를 WebSocket을 통해 전송
                messagingTemplate.convertAndSend("/sub/matching", new MatchResponse(player1Id, player2Id));
            }
        }

    }
}
