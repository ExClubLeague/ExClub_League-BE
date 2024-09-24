package com.exclub.exclub_league.Team.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAttributesDTO {
    private String activityDays; // 활동 요일
    private String activityTime; // 활동 시간대
}
