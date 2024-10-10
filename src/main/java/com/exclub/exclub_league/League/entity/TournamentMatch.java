package com.exclub.exclub_league.League.entity;

import com.exclub.exclub_league.Team.entity.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_match")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;

    @ManyToOne(optional = true)
    @JoinColumn(name = "team2_id", nullable = true)
    private Team team2;

    @Column(name = "match_date")
    private String matchDate;

    @Column(name = "city", nullable = false)
    private String city; // 행정구역 정보

    @Column(name = "match_status")
    private String matchStatus;

    @ManyToOne
    @JoinColumn(name = "winner_team_id")
    private Team winnerTeam; // 승리 팀 ID

    @Column(name = "round_number", nullable = false)
    private int roundNumber; // 현재 라운드 번호
}