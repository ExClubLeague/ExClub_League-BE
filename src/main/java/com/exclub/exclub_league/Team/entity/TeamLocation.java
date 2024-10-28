package com.exclub.exclub_league.Team.entity;

import com.exclub.exclub_league.League.entity.RegionCoordinates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TeamLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city; // 팀의 활동 도시

    private String region; // 팀의 활동 지역

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "region_coordinates_id")
    private RegionCoordinates regionCoordinates;
}
