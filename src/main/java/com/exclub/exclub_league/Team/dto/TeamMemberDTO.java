package com.exclub.exclub_league.Team.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberDTO {
    private Long id;          // 사용자 ID
    private String username;  // 사용자 이름으로 변경
    private Integer age;      // 사용자 나이
    private String gender;    // 사용자 성별

}
