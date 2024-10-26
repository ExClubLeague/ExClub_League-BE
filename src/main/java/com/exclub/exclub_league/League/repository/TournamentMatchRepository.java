package com.exclub.exclub_league.League.repository;

import com.exclub.exclub_league.League.entity.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Long> {

    // 특정 라운드와 도시에서 완료된 경기를 가져오는 메소드
    List<TournamentMatch> findByRoundNumberAndCityAndMatchStatus(int roundNumber, String city, String matchStatus);

    // 도시별로 가장 높은 라운드 번호를 찾는 메소드
    @Query("SELECT MAX(m.roundNumber) FROM TournamentMatch m WHERE m.city = :city")
    Integer findMaxRoundNumberByCity(@Param("city") String city);

    // team1 또는 team2가 teamId와 같고, 가장 큰 roundNumber를 가진 매치 조회
    @Query("SELECT tm FROM TournamentMatch tm " +
            "WHERE (tm.team1.id = :teamId OR tm.team2.id = :teamId) " +
            "AND tm.roundNumber = (SELECT MAX(tm2.roundNumber) FROM TournamentMatch tm2 " +
            "WHERE tm2.team1.id = :teamId OR tm2.team2.id = :teamId)")
    TournamentMatch findNextMatchByTeamId(@Param("teamId") Long teamId);

    // team1 또는 team2가 teamId와 같은 모든 매치 조회
    List<TournamentMatch> findByTeam1_IdOrTeam2_Id(Long teamId1, Long teamId2);

    Optional<TournamentMatch> findByTeam1_IdAndTeam2_Id(Long team1Id, Long team2Id);

    // 최대 roundNumber 조회
    @Query("SELECT MAX(tm.roundNumber) FROM TournamentMatch tm")
    Integer findMaxRoundNumber();

    // 특정 roundNumber의 매치 조회
    List<TournamentMatch> findByRoundNumber(Integer roundNumber);
}