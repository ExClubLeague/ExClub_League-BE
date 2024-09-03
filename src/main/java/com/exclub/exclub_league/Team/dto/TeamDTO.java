package com.exclub.exclub_league.Team.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDTO {
    private Long id; // 팀 ID
    private String name; // 팀 이름
    private String code; // 팀 코드
    private String logoUrl; // 팀 로고 URL
    private Long stadiumId; // 경기장 ID
    private Long locationId; // 위치 ID
    private String ageGroup; // 주요 나이대
    private String gender; // 성별
    private String skillLevel; // 팀 실력
    private TeamPerformanceDTO performance; // 경기력 정보
    private TeamAttributesDTO attributes; // 활동 요일/시간대 정보
}