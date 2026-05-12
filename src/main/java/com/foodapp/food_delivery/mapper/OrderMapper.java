package com.foodapp.food_delivery.mapper;

import com.foodapp.food_delivery.dto.OrderItemResponse;
import com.foodapp.food_delivery.dto.OrderResponse;
import com.foodapp.food_delivery.model.Order;
import com.foodapp.food_delivery.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

//    private final RestaurantMapper restaurantMapper;
//    private final MenuItemMapper menuItemMapper;
//    public OrderMapper(RestaurantMapper restaurantMapper, MenuItemMapper menuItemMapper) {
//        this.restaurantMapper = restaurantMapper;
//        this.menuItemMapper = menuItemMapper;
//    }
    public OrderResponse orderToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setPaymentStatus(order.getPaymentStatus());
        orderResponse.setPlacedAt(order.getPlacedAt());
        if(order.getDeliveredAt() != null) {
            orderResponse.setDeliveredAt(order.getDeliveredAt());
        }
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setUserId(order.getUser().getId());
        if(order.getDeliveryPartner() != null) {
            orderResponse.setDeliveryPartnerId(order.getDeliveryPartner().getId());
        }
        orderResponse.setRestaurantId(order.getRestaurant().getId());
        orderResponse.setRestaurantName(order.getRestaurant().getName());
        orderResponse.setOrderItemsResponse(orderItemsToOrderItemResponse(order.getOrderItems()));
        return orderResponse;
    }

    public List<OrderItemResponse> orderItemsToOrderItemResponse(List<OrderItem> orderItems) {
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setId(orderItem.getId());
            orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());
            orderItemResponse.setName(orderItem.getName());
            orderItemResponse.setPrice(orderItem.getPrice());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponseList.add(orderItemResponse);
        }
        return orderItemResponseList;
    }
}
