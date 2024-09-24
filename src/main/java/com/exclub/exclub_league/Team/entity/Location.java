package com.exclub.exclub_league.Team.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 위치 정보를 식별하기 위한 고유 ID (예: 1, 2, 3)

    private String city; // 팀의 활동 도시 (예: "Seoul", "Incheon")

    private String district; // 팀의 활동 지역 또는 구 (예: "Gangnam-gu", "Yeongdeungpo-gu")
}