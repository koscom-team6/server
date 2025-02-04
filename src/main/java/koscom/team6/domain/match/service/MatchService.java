package koscom.team6.domain.match.service;

import koscom.team6.domain.match.Entity.MatchReference;
import koscom.team6.domain.match.dto.request.MatchingSaveRequest;
import koscom.team6.domain.match.dto.response.*;
import koscom.team6.domain.match.repository.MatchReferenceRepository;
import koscom.team6.domain.matching.dto.ArenaObject;
import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.match.Entity.Matching;
import koscom.team6.domain.match.Entity.MatchAnswer;
import koscom.team6.domain.match.Entity.MatchHistory;
import koscom.team6.domain.match.dto.request.MatchResultRequest;
import koscom.team6.domain.match.dto.request.PracticeResultRequest;
import koscom.team6.domain.match.repository.MatchAnswerRepository;
import koscom.team6.domain.match.repository.MatchHistoryRepository;
import koscom.team6.domain.match.repository.MatchRepository;
import koscom.team6.domain.match.vo.MatchResultVO;
import koscom.team6.domain.user.dto.CustomUserDetails;
import koscom.team6.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchReferenceRepository matchReferenceRepository;
    private final MatchHistoryRepository matchHistoryRepository;
    private final MatchAnswerRepository matchAnswerRepository;
    private final RedisTemplate<String, ArenaObject> sessions;

    private final WebClient openAIWebClient;

    public MatchingSaveResponse createMatch(MatchingSaveRequest request) {
        Matching matching = Matching.of(request.getTitle(),
                request.getContent(),
                request.getImageUrl1(),
                request.getImageUrl2(),
                request.getImageUrl3(),
                request.getImageUrl4(),
                request.getTag1(),
                request.getTag2(),
                request.getTag3());

        Matching saved = matchRepository.save(matching);
        //return matching.getId();

        MatchingSaveResponse response = new MatchingSaveResponse(saved.getId(), saved.getTitle(), saved.getContent(), saved.getImageUrl1(),
                saved.getImageUrl2(), saved.getImageUrl3(), saved.getImageUrl4(), saved.getTag1(), saved.getTag2(), saved.getTag3());
        return response;
    }

    public PracticeResponse getPractice(CustomUserDetails userDetails) {
//        createMatch(request);
        UserEntity user = userRepository.findByUsername(userDetails.getUsername());
        return matchRepository.findById(1L)
                .map(practice -> PracticeResponse.of(user, practice))
                .orElseThrow(() -> new IllegalArgumentException("Practice not found"));
    }

    public PracticeResultResponse getPracticeResult(CustomUserDetails userDetails, PracticeResultRequest practiceResultRequest) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername());

        String prompt = "아래 요구조건에 맞춰서 대답해줘.";
        String userAIAnswer = getAIAnswer(practiceResultRequest.getUserAnswer()).block();

        Integer userScore = 10;

        Matching matching = matchRepository.findById(practiceResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Matching not found"));

        // 유저 연습게임 횟수 기록

        return PracticeResultResponse.of(user, practiceResultRequest.getUserAnswer(), userAIAnswer, userScore);
    }

    public MatchResponse getMatch(String matchSessionId) {
//        createMatch();

        ArenaObject arenaObject = sessions.opsForValue().get(matchSessionId);
        Matching matching = matchRepository.findById(arenaObject.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("Matching not found"));
        List<MatchReference> matchReferences = matchReferenceRepository.findAllByMatching(matching);

        return MatchResponse.of(matching, matchReferences);
    }

    public MatchResultResponse getMatchResult(CustomUserDetails userDetails, MatchResultRequest matchResultRequest) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername());
        UserEntity rival = userRepository.findById(matchResultRequest.getRivalId());

        // GPT 답안 로직 추가 -> 동점 안나오게 처리
        String userAIAnswer = getAIAnswer(matchResultRequest.getUserAnswer()).block();
        String rivalAIAnswer = getAIAnswer(matchResultRequest.getRivalAnswer()).block();
        Integer userScore = 10;
        Integer rivalScore = 0;

        Matching matching = matchRepository.findById(matchResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Matching not found"));
        MatchHistory matchHistory = MatchHistory.of(matching, user, rival);
        matchHistoryRepository.save(matchHistory);

        MatchAnswer userAnswer = MatchAnswer.of(matchHistory, user, matchResultRequest.getUserAnswer(), userAIAnswer);
        MatchAnswer rivalAnswer = MatchAnswer.of(matchHistory, rival, matchResultRequest.getRivalAnswer(), rivalAIAnswer);
        matchAnswerRepository.save(userAnswer);
        matchAnswerRepository.save(rivalAnswer);

        // 승자/패자 처리
        int addScore = logarithmicScore(user);
        if (userScore > rivalScore) {
            user.setWinner(addScore);
            userRepository.save(user);
            rival.setLoser();
            userRepository.save(rival);
        }
        else {
            rival.setWinner(addScore);
            userRepository.save(rival);
            user.setLoser();
            userRepository.save(user);
        }
        MatchResultVO userResult = MatchResultVO.of(userAnswer, userScore);
        MatchResultVO rivalResult = MatchResultVO.of(rivalAnswer, rivalScore);
        return MatchResultResponse.of(user, userResult, rivalResult, addScore);
    }

    private int logarithmicScore(UserEntity user) {
        int time = user.getSolvedCount();
        int maxScore = 30;
        int minScore = 5;
        double decayFactor = 3.0;

        return (int) (minScore + (maxScore - minScore) / (1 + Math.log(time + 1) / decayFactor));
    }

    private Mono<String> getAIAnswer(String userAnswer) {
        return openAIWebClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-4",
                        "messages", List.of(Map.of("role", "user", "content", userAnswer)),
                        "max_tokens", 100
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> firstChoice = choices.get(0);
                        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                        return (String) message.get("content");
                    }
                    return "No response from OpenAI.";
                });
    }
}
