package com.foodapp.food_delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Long userId;
    private List<CartItem> cartItems;
    private Long restaurantId;


    public Double calculateTotalPrice(){
        List<CartItem> cartItems = this.getCartItems();
        Double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getPrice() *  cartItem.getQuantity();
        }
        return totalPrice;
    }
}
