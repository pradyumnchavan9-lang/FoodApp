package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.CuisineType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String phone;
    @NotNull
    private CuisineType cuisineType;
    @NotNull
    private AddressRequest address;

}
