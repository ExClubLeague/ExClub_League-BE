package com.exclub.exclub_league.Team.repository;

import com.exclub.exclub_league.Team.entity.Stadium;
import com.exclub.exclub_league.Team.entity.TeamPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPerformanceRepository extends JpaRepository<TeamPerformance, Long> {
}
