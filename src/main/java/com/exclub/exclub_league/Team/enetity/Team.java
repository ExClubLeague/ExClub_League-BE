package com.exclub.exclub_league.Team.enetity;
import com.exclub.exclub_league.User.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;  // 팀의 고유 식별자 (자동 증가 ID)

    @Column(nullable = false, unique = true)
    private String teamName;  // 팀 이름 (중복 불가, 필수 입력)

    private String description;  // 팀 설명 (선택 사항)

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 팀 생성 시간 (수정 불가)

    private LocalDateTime updatedAt = LocalDateTime.now();  // 팀 정보 마지막 수정 시간

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;  // 팀 생성자 (필수 입력, User 엔티티와 다대일 관계)

    private Boolean isActive = true;  // 팀 활성화 상태 (기본값: true)

    private String teamLogo;  // 팀 로고 이미지 URL 또는 파일 경로 (선택 사항)

    private String location;  // 팀 위치 또는 지역 (선택 사항)

    private String venue;  // 팀의 경기장 또는 모임 장소 (선택 사항)

    private String gender;  // 팀의 성별 (예: 남성, 여성, 혼성 등)

    private String sport;  // 팀이 속한 스포츠 종목 (예: 축구, 농구 등)

    private String ageGroup;  // 팀의 연령대 (예: 성인, 청소년 등)

    private String schedule;  // 팀의 경기 일정 또는 연습 일정

    private String skillLevel;  // 팀의 실력 수준 (예: 초급, 중급, 고급 등)
}