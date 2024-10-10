package com.exclub.exclub_league.Team.entity;
import com.exclub.exclub_league.League.entity.RegionCoordinates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 위치 정보를 식별하기 위한 고유 ID (예: 1, 2, 3)

    private String city; // 팀의 활동 도시 (예: 서울시, 경기도)

    private String region; // 팀의 활동 지역 또는 구 (예: 종로구, 광진구)

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "region_coordinates_id")
    private RegionCoordinates regionCoordinates;

    public Location(String city, String region, RegionCoordinates regionCoordinates) {
        this.city = city;
        this.region = region;
        this.regionCoordinates = regionCoordinates;
    }
}