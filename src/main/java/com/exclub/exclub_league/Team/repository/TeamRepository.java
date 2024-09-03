package com.exclub.exclub_league.Team.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exclub.exclub_league.Team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
