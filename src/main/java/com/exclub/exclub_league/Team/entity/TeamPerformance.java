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
public class TeamPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 팀 성능 정보를 식별하기 위한 고유 ID (예: 1, 2, 3)

    private int attack; // 공격 능력 (0-100 범위, 예: 75)

    private int pass; // 패스 능력 (0-100 범위, 예: 80)

    private int dribble; // 드리블 능력 (0-100 범위, 예: 70)

    private int physical; // 피지컬 능력 (0-100 범위, 예: 85)

    private int defense; // 수비 능력 (0-100 범위, 예: 78)

    private int shoot; // 슛 능력 (0-100 범위, 예: 90)

    private int speed; // 스피드 능력 (0-100 범위, 예: 88)

    private int stamina; // 체력 능력 (0-100 범위, 예: 82)
}

