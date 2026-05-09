package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.MenuCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequest {

    @NotBlank
    private String name;
    @NotNull
    private Double price;
    @NotNull
    private MenuCategory category;
    @NotNull
    private Boolean isVeg;
    private Double serves;
    private String portionSize;
    private Boolean isAvailable;

}
