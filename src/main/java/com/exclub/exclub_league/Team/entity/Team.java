package com.exclub.exclub_league.Team.entity;
import com.exclub.exclub_league.User.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id; // 팀을 식별하기 위한 고유 ID

    @Column(name = "TEAM_NAME")
    private String name; // 팀 이름 O

    @Column(name = "CODE")
    private String code; // 팀 코드 O

    @Column(name = "TEAM_LOGO")
    private String logoUrl; // 팀 로고 이미지의 URL O

    @Column(name = "VENUE")
    private String venue; // 경기장 정보 O

    @Column(name = "AGE_GROUP")
    private String ageGroup; // 팀의 주요 나이대 O

    @Column(name = "GENDER")
    private String gender; // 팀의 성별 O

    @Column(name = "SKILL_LEVEL")
    private String skillLevel; // 팀의 실력 레벨 O

    @Column(name = "DESCRIPTION")
    private String description; // 팀 설명

    @Column(name = "IS_ACTIVE")
    private Boolean isActive; // 팀의 활동 상태

    @ManyToOne
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "id")
    private User createdBy; // 팀을 생성한 사용자 O

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt; // 생성 시간 O

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt; // 수정 시간 O

    @Column(name = "IS_LEAGUE_APPLICANT")
    private boolean isLeagueApplicant; // 리그 신청 여부

    @OneToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "TEAM_LOCATION_ID")
    private TeamLocation location;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "STADIUM_ID")
    private Stadium stadium;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "attributes_id")
    private TeamAttributes attributes;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "performance_id")
    private TeamPerformance performance;

    @PrePersist
    public void prePersist() { // 생성 시간 설정하는 부분
        this.createdAt = LocalDateTime.now();
    }
}