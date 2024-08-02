package com.exclub.exclub_league.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String state;
    private String city;

    @OneToMany(mappedBy = "address")
    private Set<User> users = new HashSet<>();
}
