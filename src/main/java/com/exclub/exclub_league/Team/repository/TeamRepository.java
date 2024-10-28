package com.exclub.exclub_league.Team.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exclub.exclub_league.Team.entity.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByIsLeagueApplicantTrueAndLocation_City(String city);

    @Query("SELECT t.name FROM Team t WHERE t.id = :id")
    String findNameById(@Param("id") Long id);
}
