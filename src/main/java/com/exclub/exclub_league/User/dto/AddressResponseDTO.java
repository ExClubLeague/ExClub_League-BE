package com.exclub.exclub_league.User.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {
    private Long addressId;
    private String state;
    private String city;
    private int radius;
}