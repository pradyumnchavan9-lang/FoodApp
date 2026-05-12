package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.OrderStatus;
import com.foodapp.food_delivery.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime placedAt;
    private LocalDateTime deliveredAt;
    private Long userId;
    private Long deliveryPartnerId;
    private Long  restaurantId;
    private String restaurantName;
    private List<OrderItemResponse> orderItemsResponse;
    private Double totalPrice;
}
