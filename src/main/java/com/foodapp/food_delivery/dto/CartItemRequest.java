package com.foodapp.food_delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {

    @NotNull
    private Long menuItemId;
    @NotNull
    private Integer quantity;

}
