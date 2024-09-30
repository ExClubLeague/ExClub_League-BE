package com.exclub.exclub_league.Team.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exclub.exclub_league.Team.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByIsLeagueApplicantTrueAndLocation_City(String city);
}
