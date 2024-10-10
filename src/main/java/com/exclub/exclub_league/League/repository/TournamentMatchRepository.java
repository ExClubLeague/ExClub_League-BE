package com.exclub.exclub_league.League.repository;

import com.exclub.exclub_league.League.entity.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Long> {

    // 특정 라운드와 도시에서 완료된 경기를 가져오는 메소드
    List<TournamentMatch> findByRoundNumberAndCityAndMatchStatus(int roundNumber, String city, String matchStatus);

    // 도시별로 가장 높은 라운드 번호를 찾는 메소드
    @Query("SELECT MAX(m.roundNumber) FROM TournamentMatch m WHERE m.city = :city")
    Integer findMaxRoundNumberByCity(@Param("city") String city);

}