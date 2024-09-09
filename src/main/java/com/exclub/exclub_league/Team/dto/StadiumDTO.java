package com.exclub.exclub_league.Team.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StadiumDTO {
    private Long id; // 경기장 ID
    private String name; // 경기장 이름
    private String city; // 경기장이 위치한 도시
    private String district; // 경기장이 위치한 지역 또는 구
}