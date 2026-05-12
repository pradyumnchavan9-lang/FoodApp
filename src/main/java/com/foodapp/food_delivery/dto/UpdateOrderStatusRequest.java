package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {

    @NotNull
    private Long orderId;
    @NotNull
    private OrderStatus orderStatus;
}
