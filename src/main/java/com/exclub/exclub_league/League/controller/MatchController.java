package com.exclub.exclub_league.League.controller;

import com.exclub.exclub_league.League.entity.TournamentMatch;
import com.exclub.exclub_league.League.repository.TournamentMatchRepository;
import com.exclub.exclub_league.League.service.TeamMatchingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

    private final TeamMatchingService teamMatchingService;
    private final TournamentMatchRepository matchRepository;

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
}
