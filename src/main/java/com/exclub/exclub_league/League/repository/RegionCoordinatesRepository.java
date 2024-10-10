package com.exclub.exclub_league.League.repository;

import com.exclub.exclub_league.League.entity.RegionCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionCoordinatesRepository extends JpaRepository<RegionCoordinates, Long> {
    List<RegionCoordinates> findByCity(String city);
    Optional<RegionCoordinates> findByCityAndRegion(String city, String region);
}
