package com.exclub.exclub_league.Team.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exclub.exclub_league.Team.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByIsLeagueApplicantTrueAndLocation_City(String city);

    // 특정 도시에서 참여한 팀의 수를 카운트하는 메소드
    int countByLocation_City(String city);
}
