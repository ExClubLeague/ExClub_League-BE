package com.exclub.exclub_league.User.dto;

import java.time.LocalDateTime;

public class UserResponseDTO {
    private Long id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressResponseDTO address;  // Address 정보 포함
}