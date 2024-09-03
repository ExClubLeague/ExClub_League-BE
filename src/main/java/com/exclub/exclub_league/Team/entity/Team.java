package com.exclub.exclub_league.Team.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 팀을 식별하기 위한 고유 ID (예: 1, 2, 3)

    private String name; // 팀 이름 (예: "Seoul United", "Blue Tigers")

    private String code; // 팀 코드, 초대나 팀 식별에 사용될 수 있는 고유 코드 (예: "SU2024", "BT1234")

    private String logoUrl; // 팀 로고 이미지의 URL (예: "https://example.com/logos/seoul_united.png")

    @ManyToOne
    private Stadium stadium; // 팀이 주로 활동하는 경기장 정보 (예: "Olympic Park Stadium")

    @ManyToOne
    private Location location; // 팀의 활동 위치 정보 (예: "Seoul", "Gangnam")

    private String ageGroup; // 팀의 주요 나이대 (예: "20s", "30s")

    private String gender; // 팀의 성별 (예: "Male", "Female", "Coed")

    private String skillLevel; // 팀의 실력 레벨 (예: "Beginner", "Intermediate", "Advanced")

    @OneToOne
    private TeamPerformance performance; // 팀의 경기력 정보 (각 능력치가 포함된 TeamPerformance 객체)

    @OneToOne
    private TeamAttributes attributes; // 팀의 속성 정보 (활동 요일 및 시간대가 포함된 TeamAttributes 객체)
}