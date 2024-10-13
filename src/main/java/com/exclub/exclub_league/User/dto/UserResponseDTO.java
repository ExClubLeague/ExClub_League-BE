package com.exclub.exclub_league.User.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean joinedTeam;
   // private AddressResponseDTO address;  // Address 정보 포함
}