package com.foodapp.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;
    private Long menuItemId;
    private String name;
    private Double price;
    private Integer quantity;
}
