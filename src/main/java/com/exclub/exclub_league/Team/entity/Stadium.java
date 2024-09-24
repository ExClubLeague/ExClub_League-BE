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
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 경기장을 식별하기 위한 고유 ID (예: 1, 2, 3)

    private String name; // 경기장 이름 (예: "Olympic Park Stadium", "Jamsil Stadium")

    private String city; // 경기장이 위치한 도시 (예: "Seoul", "Busan")

    private String district; // 경기장이 위치한 지역 또는 구 (예: "Songpa-gu", "Gangnam-gu")
}