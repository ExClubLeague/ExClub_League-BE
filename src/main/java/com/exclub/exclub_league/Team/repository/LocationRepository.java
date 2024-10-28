package com.exclub.exclub_league.Team.repository;
import com.exclub.exclub_league.Team.entity.Location;
import com.exclub.exclub_league.Team.entity.TeamLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<TeamLocation, Long> {
    // 특정 city 값을 가진 TeamLocation의 수를 반환하는 메소드
    int countByCity(String city);
}