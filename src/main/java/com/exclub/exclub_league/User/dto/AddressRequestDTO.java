package com.exclub.exclub_league.User.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequestDTO {
    private String state;
    private String city;
    private int radius;
}