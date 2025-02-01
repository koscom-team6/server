package koscom.team6.domain.match.service;

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

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchHistoryRepository matchHistoryRepository;
    private final MatchAnswerRepository matchAnswerRepository;

    private void createMatch() {
        Match match = Match.of("title", "content", "additionalInfo", "imageUrl1", "imageUrl2", "imageUrl3", "imageUrl4");
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

        // GPT 답안 로직 추가
        String userAIAnswer = "AIAnswer";

        Integer userScore = 10;

        Match match = matchRepository.findById(practiceResultRequest.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        // 유저 연습게임 횟수 기록

        return PracticeResultResponse.of(user, practiceResultRequest.getUserAnswer(), userAIAnswer, userScore);
    }

    public MatchResponse getMatch(CustomUserDetails userDetails, Long rivalId, Long roomId) {
        createMatch();
        UserEntity rival = userRepository.findById(rivalId);
        // 문제 선택 로직 추가
        return matchRepository.findById(1L)
                .map(MatchResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
    }

    public MatchResultResponse getMatchResult(CustomUserDetails userDetails, MatchResultRequest matchResultRequest) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername());
        UserEntity rival = userRepository.findById(matchResultRequest.getRivalId());

        // GPT 답안 로직 추가 -> 동점 안나오게 처리
        String userAIAnswer = "AIAnswer";
        String rivalAIAnswer = "rivalAIAnswer";
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
}
