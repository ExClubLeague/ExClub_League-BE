package com.exclub.exclub_league.League.service;

import com.exclub.exclub_league.League.dto.MatchResultUpdateDTO;
import com.exclub.exclub_league.League.dto.PreviousMatchResultDTO;
import com.exclub.exclub_league.League.dto.NextMatchDTO;
import com.exclub.exclub_league.League.entity.TournamentMatch;
import com.exclub.exclub_league.League.repository.TournamentMatchRepository;
import com.exclub.exclub_league.Team.entity.Team;
import com.exclub.exclub_league.Team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {

    @Autowired
    TournamentMatchRepository tournamentMatchRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    private TeamMatchingService teamMatchingService;

    public NextMatchDTO getNextMatch(Long teamId) {

        // 1. TournamentMatch에서 roundNumber가 가장 큰 매치 중에 해당 팀이 포함된 경기 찾기
        TournamentMatch nextMatch = tournamentMatchRepository.findNextMatchByTeamId(teamId);

        if (nextMatch == null) {
            return null; // 또는 적절한 예외를 던질 수 있음
        }

        // 2. 상대팀 ID 찾기 (team1 또는 team2 중에서 teamId와 다른 ID를 선택)
        Long opponentTeamId = getOpponentTeamId(nextMatch, teamId);

        // 2-1. 상대팀 이름 조회
        String opponentTeamName = teamRepository.findNameById(opponentTeamId);

        // 3. 몇 강인지 계산 (roundNumber에 따라 16강, 8강, 4강 등 계산)
        String stage = calculateStage(nextMatch.getRoundNumber());

        // 4. 경기 날짜 가져오기
        String matchDate = nextMatch.getMatchDate();

        // 5. 팀1, 팀2의 venue 중 랜덤하게 선택
        String venue = selectRandomVenue(nextMatch.getTeam1().getVenue(), nextMatch.getTeam2().getVenue());

        // 결과 반환
        return new NextMatchDTO(opponentTeamName, stage, matchDate, venue);
    }

    // 상대팀 ID를 찾는 메소드
    private Long getOpponentTeamId(TournamentMatch match, Long teamId) {
        return match.getTeam1().getId().equals(teamId) ? match.getTeam2().getId() : match.getTeam1().getId();
    }

    // 몇 강인지 계산하는 메소드 (roundNumber에 따라 결정)
    private String calculateStage(int roundNumber) {
        switch (roundNumber) {
            case 1: return "16강";
            case 2: return "8강";
            case 3: return "4강";
            case 4: return "결승";
            default: return "Unknown Stage";
        }
    }

    // 랜덤으로 venue 선택하는 메소드
    private String selectRandomVenue(String venue1, String venue2) {
        Random random = new Random();
        return random.nextBoolean() ? venue1 : venue2;
    }

    public List<PreviousMatchResultDTO> getPreviousMatchResults(Long teamId) {
        // 1. 해당 teamId가 포함된 이전 매치들 조회
        List<TournamentMatch> matches = tournamentMatchRepository.findByTeam1_IdOrTeam2_Id(teamId, teamId);

        // 2. 매치가 없으면 null 반환
        if (matches.isEmpty()) {
            return null;
        }

        List<PreviousMatchResultDTO> results = new ArrayList<>();
        for (TournamentMatch match : matches) {
            // 3. winnerTeam이 null이 아닌 경우에만 추가
            if (match.getWinnerTeam() != null) {
                // 4. 상대 팀 이름 찾기
                String opponentTeamName = match.getTeam1().getId().equals(teamId)
                        ? match.getTeam2().getName()
                        : match.getTeam1().getName();

                // 5. 경기 점수 가져오기
                String score = match.getScore(); // TOURNAMENT_MATCH 테이블에 있는 점수 컬럼

                // 6. 결과 추가
                results.add(new PreviousMatchResultDTO(opponentTeamName, score));
            }
        }

        // 7. 결과가 없으면 null 반환
        return results.isEmpty() ? null : results;
    }

    public String updateMatchResult(MatchResultUpdateDTO request) {
        // 매치 조회
        Optional<TournamentMatch> optionalMatch = tournamentMatchRepository.findByTeam1_IdAndTeam2_Id(request.getTeam1Id(), request.getTeam2Id());

        // 순서를 바꿔서 다시 시도
        if (optionalMatch.isEmpty()) {
            optionalMatch = tournamentMatchRepository.findByTeam1_IdAndTeam2_Id(request.getTeam2Id(), request.getTeam1Id());
        }

        // 매치가 존재하지 않는 경우 처리
        if (optionalMatch.isEmpty()) {
            return "No matches found.";
        }

        // 매치가 존재하는 경우
        TournamentMatch match = optionalMatch.get();

        // 승리팀 객체 조회
        Team winnerTeam = teamRepository.findById(request.getWinnerTeamId())
                .orElse(null);

        // 점수와 우승팀을 업데이트
        match.setScore(request.getScore());
        match.setWinnerTeam(winnerTeam);

        // 변경된 내용을 저장
        tournamentMatchRepository.save(match);

        // 현재 테이블에서 가장 큰 roundNumber 조회
        Integer maxRoundNumber = tournamentMatchRepository.findMaxRoundNumber();

        // 해당 roundNumber의 매치들 조회
        List<TournamentMatch> currentRoundMatches = tournamentMatchRepository.findByRoundNumber(maxRoundNumber);

        // winnerTeam이 null인 매치가 있는지 확인
        boolean hasNullWinnerTeam = currentRoundMatches.stream()
                .anyMatch(currentMatch -> currentMatch.getWinnerTeam() == null);

        // 모든 매치의 winnerTeam이 null이 아닌 경우
        if (!hasNullWinnerTeam) {
            // 다음 라운드 생성
            List<TournamentMatch> thirdRoundMatches = teamMatchingService.createNextRoundMatches(maxRoundNumber + 1, "city"); // "city"는 적절한 인자로 변경
        }

        return "Match results updated successfully.";
    }
}
