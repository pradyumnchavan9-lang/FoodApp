package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.MenuCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemResponse {

    private Long id;
    private String name;
    private Double price;
    private MenuCategory category;
    private Boolean isVeg;
    private Double serves;
    private String portionSize;
    private Boolean isAvailable;
    private Long restaurantId;

}
