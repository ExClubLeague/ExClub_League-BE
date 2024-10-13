package com.exclub.exclub_league.User.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String password;
    private AddressRequestDTO address;   // Address ID (참조)
}
