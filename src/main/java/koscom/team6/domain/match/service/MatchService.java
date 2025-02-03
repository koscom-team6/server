package koscom.team6.domain.match.service;

import koscom.team6.domain.match.Entity.MatchReference;
import koscom.team6.domain.match.repository.MatchReferenceRepository;
import koscom.team6.domain.user.Entity.UserEntity;
import koscom.team6.domain.match.Entity.Match;
import koscom.team6.domain.match.Entity.MatchAnswer;
import koscom.team6.domain.match.Entity.MatchHistory;
import koscom.team6.domain.match.dto.request.MatchResultRequest;
import koscom.team6.domain.match.dto.request.PracticeResultRequest;
import koscom.team6.domain.match.dto.response.MatchResponse;
import koscom.team6.domain.match.dto.response.MatchResultResponse;
import koscom.team6.domain.match.dto.response.PracticeResponse;
import koscom.team6.domain.match.dto.response.PracticeResultResponse;
import koscom.team6.domain.match.repository.MatchAnswerRepository;
import koscom.team6.domain.match.repository.MatchHistoryRepository;
import koscom.team6.domain.match.repository.MatchRepository;
import koscom.team6.domain.match.vo.MatchResultVO;
import koscom.team6.domain.user.dto.CustomUserDetails;
import koscom.team6.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    private final WebClient openAIWebClient;

    private void createMatch() {
        Match match = Match.of("title", "content", "imageUrl1", "imageUrl2", "imageUrl3", "imageUrl4", "tag1", "tag2", "tag3");
        matchRepository.save(match);
        //return match.getId();
    }

    public PracticeResponse getPractice(CustomUserDetails userDetails) {
        createMatch();
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

        Match match = matchRepository.findById(practiceResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        // 유저 연습게임 횟수 기록

        return PracticeResultResponse.of(user, practiceResultRequest.getUserAnswer(), userAIAnswer, userScore);
    }

    public MatchResponse getMatch(CustomUserDetails userDetails, Long matchId) {
        createMatch();
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        List<MatchReference> matchReferences = matchReferenceRepository.findAllByMatch(match);

        return MatchResponse.of(match, matchReferences);
    }

    public MatchResultResponse getMatchResult(CustomUserDetails userDetails, MatchResultRequest matchResultRequest) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername());
        UserEntity rival = userRepository.findById(matchResultRequest.getRivalId());

        // GPT 답안 로직 추가 -> 동점 안나오게 처리
        String userAIAnswer = getAIAnswer(matchResultRequest.getUserAnswer()).block();
        String rivalAIAnswer = getAIAnswer(matchResultRequest.getRivalAnswer()).block();
        Integer userScore = 10;
        Integer rivalScore = 0;

        Match match = matchRepository.findById(matchResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        MatchHistory matchHistory = MatchHistory.of(match, user, rival);
        matchHistoryRepository.save(matchHistory);

        MatchAnswer userAnswer = MatchAnswer.of(matchHistory, user, matchResultRequest.getUserAnswer(), userAIAnswer);
        MatchAnswer rivalAnswer = MatchAnswer.of(matchHistory, rival, matchResultRequest.getRivalAnswer(), rivalAIAnswer);
        matchAnswerRepository.save(userAnswer);
        matchAnswerRepository.save(rivalAnswer);
        MatchResultVO userResult = MatchResultVO.of(userAnswer, userScore);
        MatchResultVO rivalResult = MatchResultVO.of(rivalAnswer, rivalScore);

        // 승자/패자 처리
        int addScore = logarithmicScore(user);
        UserEntity winner = userScore > rivalScore ? user : rival;
        UserEntity loser = userScore > rivalScore ? rival : user;
        winner.setWinner(addScore);
        userRepository.save(winner);
        loser.setLoser();
        userRepository.save(loser);

        return MatchResultResponse.of(user, userResult, rivalResult);
    }

    private int logarithmicScore(UserEntity user) {
        int time = user.getSolvedCount();
        int scale = 10;
        return (int) (scale * Math.log(time + 1));
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
