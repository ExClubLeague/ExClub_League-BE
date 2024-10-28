package com.exclub.exclub_league.League.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreviousMatchResultDTO {
    private String opponentTeamName; // 상대팀 이름
    private String score; // 경기 점수
}
