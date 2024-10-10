package com.exclub.exclub_league.League.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "region_coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionCoordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region", nullable = false)
    private String region; // ex) 종로구

    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude; // ex) 위도

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude; // ex) 경도

    @Column(name = "city", nullable = false)
    private String city; // ex)서울시

    public RegionCoordinates(String city, BigDecimal latitude, BigDecimal longitude, String region) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }
}
