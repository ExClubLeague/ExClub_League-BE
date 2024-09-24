package com.exclub.exclub_league.Team.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private String city; // 활동 도시
    private String district; // 활동 지역 또는 구
}
