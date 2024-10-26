package com.exclub.exclub_league.League.controller;

import com.exclub.exclub_league.League.dto.MatchResultUpdateDTO;
import com.exclub.exclub_league.League.dto.NextMatchDTO;
import com.exclub.exclub_league.League.dto.PreviousMatchResultDTO;
import com.exclub.exclub_league.League.entity.TournamentMatch;
import com.exclub.exclub_league.League.repository.TournamentMatchRepository;
import com.exclub.exclub_league.League.service.MatchService;
import com.exclub.exclub_league.League.service.TeamMatchingService;
import com.exclub.exclub_league.exception.MatchNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

    private final TeamMatchingService teamMatchingService;
    private final TournamentMatchRepository matchRepository;
    private final MatchService matchService;

    // 첫번째 라운드 생성
    @PostMapping("/createFirstRound")
    public ResponseEntity<String> createFirstRoundMatches(@RequestParam String city) {
        try {
            // 매칭 생성
            List<TournamentMatch> matches = teamMatchingService.createFirstRoundMatches(city);

            // 매칭 결과를 데이터베이스에 저장
            for (TournamentMatch match : matches) {
                matchRepository.save(match);
            }

            return ResponseEntity.ok("First round matches created and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating first round matches: " + e.getMessage());
        }
    }

    // 매칭된 팀의 경기날짜 저장
    @PutMapping("/{matchId}/updateDate")
    public ResponseEntity<String> updateMatchDate(
            @PathVariable Long matchId,
            @RequestParam String matchDate) {
        try {
            // 사용자가 전달한 문자열을 LocalDateTime으로 변환
           // LocalDateTime parsedMatchDate = LocalDateTime.parse(matchDate);

            // 매칭 날짜 업데이트
            teamMatchingService.updateMatchDate(matchId, matchDate);
            return ResponseEntity.ok("Match date updated successfully.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Invalid date format. Please use the format 'yyyy-MM-ddTHH:mm:ss'.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating match date: " + e.getMessage());
        }
    }

    // 1 round에서 승리팀 저장
    @PutMapping("/{matchId}/updateResult/{winnerTeamId}")
    public ResponseEntity<String> updateMatchResult(
            @PathVariable Long matchId,
            @PathVariable Long winnerTeamId) {
        try {
            // 경기 결과 업데이트
            teamMatchingService.updateMatchResult(matchId, winnerTeamId);
            return ResponseEntity.ok("Match result updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating match result: " + e.getMessage());
        }
    }

    @PostMapping("/next/{teamId}")
    public NextMatchDTO getNextMatch(@PathVariable Long teamId) {
        try {
            // 다음 경기 정보 조회
            return matchService.getNextMatch(teamId);
        } catch (MatchNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "다음 경기를 찾을 수 없습니다.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", e);
        }
    }

    @PostMapping("/previous/{teamId}")
    public ResponseEntity<List<PreviousMatchResultDTO>> getPreviousMatches(@PathVariable Long teamId) {
        // 이전 경기 결과를 가져오는 서비스 메소드 호출
        List<PreviousMatchResultDTO> previousMatches = matchService.getPreviousMatchResults(teamId);

        // 결과가 null이면 204 No Content 상태 반환, 아니면 200 OK 상태와 함께 결과 반환
        if (previousMatches == null || previousMatches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(previousMatches);
    }

    @PostMapping("/updateResult")
    public ResponseEntity<String> updateMatchResult(@RequestBody MatchResultUpdateDTO request) {
        String responseMessage = matchService.updateMatchResult(request);

        if (responseMessage.equals("No matches found.")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseMessage);
    }
}
