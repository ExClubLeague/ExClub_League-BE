package com.exclub.exclub_league.League.repository;

import com.exclub.exclub_league.League.entity.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Long> {

    // 특정 라운드와 도시에서 완료된 경기를 가져오는 메소드
    List<TournamentMatch> findByRoundNumberAndCityAndMatchStatus(int roundNumber, String city, String matchStatus);

}