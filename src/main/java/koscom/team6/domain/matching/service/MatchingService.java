package koscom.team6.domain.matching.service;

import jakarta.transaction.Transactional;
import koscom.team6.domain.match.Entity.Matching;
import koscom.team6.domain.match.dto.request.MatchResultRequest;
import koscom.team6.domain.match.dto.response.MatchResultResponse;
import koscom.team6.domain.match.repository.MatchRepository;
import koscom.team6.domain.match.service.MatchService;
import koscom.team6.domain.matching.dto.*;
import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.user.dto.CustomUserDetails;
import koscom.team6.domain.user.repository.UserRepository;
import koscom.team6.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final RedisTemplate<String, String> redisQueue;
    private final RedisTemplate<String, ArenaObject> sessions;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchService matchService;

    private static final String QUEUE_KEY = "matchingQueue";

    @Transactional
    public void requestMatching(String token) {

        // Redis 큐에 플레이어 추가
        redisQueue.opsForList().rightPush(QUEUE_KEY, token);

        // 큐의 크기 확인
        Long queueSize = redisQueue.opsForList().size(QUEUE_KEY);

        // 두 명의 플레이어가 매칭되었는지 확인
        if (queueSize != null && queueSize >= 2) {

            // 두 명의 플레이어를 큐에서 꺼냄
            String player1Token = redisQueue.opsForList().leftPop(QUEUE_KEY);
            String player2Token = redisQueue.opsForList().leftPop(QUEUE_KEY);

            System.out.println("player1Token = " + player1Token);
            System.out.println("player2Token = " + player2Token);

            if (player1Token != null && player2Token != null) {
                String matchSessionId = UUID.randomUUID().toString();

                String username1 = jwtUtil.getUsername(player1Token);
                String username2 = jwtUtil.getUsername(player2Token);

                ArenaObject object = new ArenaObject();

                UserEntity user1 = userRepository.findByUsername(username1);
                UserEntity user2 = userRepository.findByUsername(username2);

                object.setUser1(user1);
                object.setUser2(user2);

                Matching matching = matchRepository.findById(1L).get();

                object.setProblemId(matching.getId());

                object.setSolved1(false);
                object.setSolved2(false);

                saveArenaObject(matchSessionId, object);

                // 매칭 결과를 WebSocket을 통해 전송
                messagingTemplate.convertAndSend("/sub/matching", new MatchingResponse(user1.getId(), user2.getId(), matchSessionId));
            }
        }

    }

    public void saveArenaObject(String matchSessionId, ArenaObject object) {

        sessions.opsForValue().set(matchSessionId, object);
    }

    public ArenaObject getArenaObject(String matchSessionId) {
        return sessions.opsForValue().get(matchSessionId);
    }

    public ArenaObject getMessage(ArenaRequest request) {

        String type = request.getType();
        String username = jwtUtil.getUsername(request.getToken());
        UserEntity user = userRepository.findByUsername(username);

        ArenaObject arenaObject = getArenaObject(request.getMatchSessionId());

        arenaObject.setMatchSessionId(request.getMatchSessionId());
        arenaObject.setType(request.getType() + " " + user.getNickname());

        if (type.equals("solved")) {

            if (user.getId().equals(arenaObject.getUser1().getId())) {
                arenaObject.setSolved1(true);
                arenaObject.setContent1(request.getContent());
            } else {
                arenaObject.setSolved2(true);
                arenaObject.setContent2(request.getContent());
            }
            saveArenaObject(request.getMatchSessionId(), arenaObject);

            if (arenaObject.isSolved1() && arenaObject.isSolved2()) {

                CustomUserDetails userDetails1 = new CustomUserDetails(arenaObject.getUser1());

                MatchResultResponse matchResult = matchService.getMatchResult(userDetails1, MatchResultRequest.builder()
                        .matchId(arenaObject.getProblemId())
                        .rivalId(arenaObject.getUser2().getId())
                        .userAnswer(arenaObject.getContent1())
                        .rivalAnswer(arenaObject.getContent2())
                        .build());

                arenaObject.setResult1(matchResult.getUserResult()); // 채점점수
                arenaObject.setResult2(matchResult.getRivalResult());

                saveArenaObject(request.getMatchSessionId(), arenaObject);

            }

        }

        messagingTemplate.convertAndSend("/sub/arena", arenaObject);

        return arenaObject;
    }
}
