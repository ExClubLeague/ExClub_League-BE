package com.exclub.exclub_league.User.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthDate;

}
