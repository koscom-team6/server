package koscom.team6.domain.match.service;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Matching matching = Matching.of(
                request.getTitle(),
                request.getContent(),
                request.getImageUrl1(), request.getImageDescription1(),
                request.getImageUrl2(), request.getImageDescription2(),
                request.getImageUrl3(), request.getImageDescription3(),
//                request.getImageUrl4(), request.getImageDescription4(),
                request.getTag1(),
                request.getTag2(),
                request.getTag3(),
                request.getReference1(), request.getReferenceLink1(),
                request.getReference2(), request.getReferenceLink2(),
                request.getReference3(), request.getReferenceLink3());

        Matching saved = matchRepository.save(matching);

        MatchingSaveResponse response = new MatchingSaveResponse(
                saved.getId(), saved.getTitle(), saved.getContent(),
                saved.getImageUrl1(), saved.getImageDescription1(),
                saved.getImageUrl2(), saved.getImageDescription2(),
                saved.getImageUrl3(), saved.getImageDescription3(),
//                saved.getImageUrl4(), saved.getImageDescription4(),
                saved.getTag1(), saved.getTag2(), saved.getTag3(),
                saved.getReference1(), saved.getReferenceLink1(),
                saved.getReference2(), saved.getReferenceLink2(),
                saved.getReference3(), saved.getReferenceLink3());
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
        Matching matching = matchRepository.findById(practiceResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Matching not found"));

        JsonObject AIAnswer = getPracticeAIAnswer(matching, practiceResultRequest.getUserAnswer());

        int userScore = AIAnswer.get("userScore").getAsInt();
        String userFeedback = AIAnswer.get("userFeedback").getAsString();

        return PracticeResultResponse.of(user, practiceResultRequest.getUserAnswer(), userFeedback, userScore);
    }

    public MatchResponse getMatch(String matchSessionId) {
//        createMatch();

        ArenaObject arenaObject = sessions.opsForValue().get(matchSessionId);
        Matching matching = matchRepository.findById(arenaObject.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("Matching not found"));

        List<MatchReferenceResponse> responses = new ArrayList<>();

        responses.add(
                MatchReferenceResponse.builder()
                        .referText(matching.getReference1())
                        .referLink(matching.getReferenceLink1())
                        .build());
        responses.add(
                MatchReferenceResponse.builder()
                        .referText(matching.getReference2())
                        .referLink(matching.getReferenceLink2())
                        .build());

        responses.add(
                MatchReferenceResponse.builder()
                        .referText(matching.getReference3())
                        .referLink(matching.getReferenceLink3())
                        .build());

        return MatchResponse.of(matching, responses);
    }

    public MatchResultResponse getMatchResult(CustomUserDetails userDetails, MatchResultRequest matchResultRequest) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername());
        UserEntity rival = userRepository.findById(matchResultRequest.getRivalId());

        Matching matching = matchRepository.findById(matchResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Matching not found"));

        JsonObject AIAnswer = getAIAnswer(matching, matchResultRequest.getUserAnswer(), matchResultRequest.getRivalAnswer());

        int userScore = AIAnswer.get("userScore").getAsInt();
        int rivalScore = AIAnswer.get("rivalScore").getAsInt();

        String userFeedback = AIAnswer.get("userFeedback").getAsString();
        String rivalFeedback = AIAnswer.get("rivalFeedback").getAsString();

        MatchHistory matchHistory = MatchHistory.of(matching, user, rival);
        matchHistoryRepository.save(matchHistory);

        MatchAnswer userAnswer = MatchAnswer.of(matchHistory, user, matchResultRequest.getUserAnswer(), userFeedback);
        MatchAnswer rivalAnswer = MatchAnswer.of(matchHistory, rival, matchResultRequest.getRivalAnswer(), rivalFeedback);
        matchAnswerRepository.save(userAnswer);
        matchAnswerRepository.save(rivalAnswer);

        // 승자-패자 처리
        int addScore = logarithmicScore(user);
        if (userScore > rivalScore) {
            user.setWinner(addScore);
            userRepository.save(user);
            rival.setLoser();
            userRepository.save(rival);
        } else {
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

    private JsonObject getPracticeAIAnswer(Matching matching, String userAnswer) {
        String title = matching.getTitle();
        String problem = matching.getContent();
        String description = matching.getImageDescription1() + " \n"
                + matching.getImageDescription2() + " \n"
                + matching.getImageDescription3() + " \n";
//                + matching.getImageDescription4() + " \n";
        String userAnswerDescription = "사용자 답안:\n" + userAnswer + "\n\n";

        String condition = "요구 조건\n" +
                "1. 사용자가 보고 배울 수 있도록 각 답안에 대한 피드백을 작성해줘. " +
                "감점 사유와 가점 사유를 함께 알려줘. " +
                "만점이 아니라면 어떤 키워드를 포함하면 더 좋을 지 함께 피드백해줘.\n" +
                "2. 아래 조건에 맞춰 예시 답안을 채점해줘. \n" +
                "- 최대점수=100, 최소점수=0, \n" +
                "- 동점 없음\n" +
                "- 채점 기준: 문제 이해도(20점 만점), 개념의 정확성(20점 만점), 논리적 전개(20점 만점), 실무적 적용 가능성(20점 만점), 추가적인 인사이트(20점 만점)" +
                "- 답안에 추가적으로 작성할 사항이 있다면 100점을 받을 수 없음\n" +
                "3. 응답을 아래와 같은 json 형태로 반환해줘.\n" +
                "응답 형태\n" +
                "{\n" +
                "\"userScore\": \n" +
                "\"userFeedback\":\n" +
                "}";

        String prompt = title + "\n" + problem + "\n" + description + "\n" + condition + "\n";

        Mono<String> AIAnswer = openAIWebClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-4",
                        "messages", List.of(Map.of("role", "user", "content", prompt)),
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
        return JsonParser.parseString(AIAnswer.toString()).getAsJsonObject();
    }

    public JsonObject getAIAnswer(Matching matching, String userAnswer, String rivalAnswer) {
        String title = matching.getTitle();
        String problem = matching.getContent();
        String description = matching.getImageDescription1() + " \n"
                + matching.getImageDescription2() + " \n"
                + matching.getImageDescription3() + " \n";
//                + matching.getImageDescription4() + " \n";
        String answers = "사용자 답안: " + userAnswer + "\n\n"
                + "상대방 답안: " + rivalAnswer + "\n\n";

        String condition =
                "지금부터 너의 역할은 사용자의 금융 상식 문제 답변을 채점하는 엄격한 채점관이야." +
                "나의 요구사항은 세가지야." +
                "1. 사용자가 보고 배울 수 있도록 각 답안에 대한 피드백을 작성해줘. " +
                "- 감점 사유와 가점 사유를 함께 알려줘.\n" +
                "- 만점이 아니라면 부족한 점을 포함하여 더 구체적으로 서술한 모범 답안을 같이 반환해줘.\n" +
                "2. 아래 조건에 맞춰 사용자 답안을 엄격하게 채점해줘.\n" +
                "- 채점 기준: 문제 이해도(20점 만점), 개념의 정확성(20점 만점), 논리적 전개(20점 만점), 실무적 적용 가능성(20점 만점), 추가적인 인사이트(20점 만점) \n" +
                "- 답안에 추가적으로 작성할 사항이 있다면 100점을 받을 수 없음\n" +
                "- '모르겠다'와 의미적으로 비슷한 답변은 0점으로 처리\n" +
                "- 동점자 없음\n" +
                "3. 응답을 아래와 같은 json 형태로 반환해줘.\n" +
                "응답 형태\n" +
                "{\n" +
                "\"userScore\": \n" +
                "\"rivalScore\":\n" +
                "\"userFeedback\":\n" +
                "\"rivalFeedback\":\n" +
                "}";

        String prompt = title + "\n" + problem + "\n" + description + "\n" + answers + "\n" + condition + "\n";

        Mono<String> AIAnswer = openAIWebClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-4",
                        "messages", List.of(Map.of("role", "user", "content", prompt))
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
        return JsonParser.parseString(Objects.requireNonNull(AIAnswer.block())).getAsJsonObject();
    }
}
