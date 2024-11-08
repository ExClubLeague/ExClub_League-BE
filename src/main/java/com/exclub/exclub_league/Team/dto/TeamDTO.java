package com.exclub.exclub_league.Team.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {
    private Long id;
    private String name; // 팀 이름
    private String code; // 팀 코드
    private String logoUrl; // 팀 로고 URL
    private StadiumDTO stadium; // 경기장 정보
    private TeamLocationDTO location; // 위치 정보
    private String ageGroup; // 주요 나이대
    private String gender; // 성별
    private String skillLevel; // 팀 실력
    private TeamPerformanceDTO performance; // 경기력 정보
    private TeamAttributesDTO attributes; // 활동 요일/시간대 정보
    private List<TeamMemberDTO> members; // 팀원 리스트
}