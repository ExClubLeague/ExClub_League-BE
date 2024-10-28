package com.exclub.exclub_league.League.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
@Getter
public class NextMatchDTO {
    private String opponentTeamName;
    private String stage;
    private String matchDate;
    private String venue;
}
