package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.CuisineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse {

    private Long id;
    private String name;
    private String description;
    private AddressResponse addressResponse;
    private OwnerResponse ownerResponse;
    private Boolean isOpen;
    private String phone;
    private CuisineType cuisineType;
    private Double rating;

}
