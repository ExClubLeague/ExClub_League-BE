package com.exclub.exclub_league.League.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResultUpdateDTO {
    private Long team1Id;        // 팀 1의 ID
    private Long team2Id;        // 팀 2의 ID
    private String score;        // 경기 점수 (예: "3:1")
    private Long winnerTeamId;   // 우승 팀 ID
}