package com.foodapp.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    private Long id;
    private String street;
    private String city;
    private String state;
    private Integer zip;
    private Double latitude;
    private Double longitude;

}
