package com.exclub.exclub_league.Team.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPerformanceDTO {
    private int attack; // 공격 능력
    private int pass; // 패스 능력
    private int dribble; // 드리블 능력
    private int physical; // 피지컬 능력
    private int defense; // 수비 능력
    private int shoot; // 슛 능력
    private int speed; // 스피드 능력
    private int stamina; // 체력 능력
}