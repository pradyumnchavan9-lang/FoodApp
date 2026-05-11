package com.foodapp.food_delivery.controller;

import com.foodapp.food_delivery.dto.CartItemRequest;
import com.foodapp.food_delivery.model.Cart;
import com.foodapp.food_delivery.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping()
    public ResponseEntity<Cart> addItem(@RequestBody CartItemRequest cartItemRequest) {
        return new ResponseEntity<>(cartService.addItem(cartItemRequest), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Cart> removeItem(@RequestBody CartItemRequest cartItemRequest) {
        return new ResponseEntity<>(cartService.removeItem(cartItemRequest), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Cart> getCart() {
        return new ResponseEntity<>(cartService.getCart(), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
