package com.foodapp.food_delivery.controller;

import com.foodapp.food_delivery.dto.OrderResponse;
import com.foodapp.food_delivery.dto.UpdateOrderStatusRequest;
import com.foodapp.food_delivery.enums.OrderStatus;
import com.foodapp.food_delivery.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(){
        return new ResponseEntity<>(orderService.checkout(), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(){
        return new ResponseEntity<>(orderService.getMyOrders(), HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@RequestBody UpdateOrderStatusRequest updateOrderStatusRequest){
        return new ResponseEntity<>(orderService.updateOrderStatus(updateOrderStatusRequest), HttpStatus.OK);
    }
}
