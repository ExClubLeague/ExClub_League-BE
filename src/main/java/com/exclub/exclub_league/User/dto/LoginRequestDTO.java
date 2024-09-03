package com.exclub.exclub_league.User.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}