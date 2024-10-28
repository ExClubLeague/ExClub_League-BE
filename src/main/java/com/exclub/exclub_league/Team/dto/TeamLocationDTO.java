package com.exclub.exclub_league.Team.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLocationDTO {

    private String city;  // 팀의 활동 도시 (예: 서울시, 경기도)

    private String region;  // 팀의 활동 지역 또는 구 (예: 종로구, 광진구)
}