package com.exclub.exclub_league.User.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String state;
    private String city;
    private int radius; // 이동 가능 최대 거리 (km 단위)

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<User> users;
}