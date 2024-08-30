package com.exclub.exclub_league.Team.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exclub.exclub_league.Team.enetity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
