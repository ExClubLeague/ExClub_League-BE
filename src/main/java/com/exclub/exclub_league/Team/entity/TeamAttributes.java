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
public class TeamAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 팀 속성 정보를 식별하기 위한 고유 ID (예: 1, 2, 3)

    private String activityDays; // 활동 요일 (예: "Mon, Wed, Fri")

    private String activityTime; // 활동 시간대 (예: "Evening", "Morning", "Afternoon")
}