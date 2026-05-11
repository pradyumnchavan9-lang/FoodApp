package com.foodapp.food_delivery.mapper;

import com.foodapp.food_delivery.dto.CartItemRequest;
import com.foodapp.food_delivery.exception.MenuItemNotFoundException;
import com.foodapp.food_delivery.model.CartItem;
import com.foodapp.food_delivery.model.MenuItem;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.repository.MenuItemRepository;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    private final MenuItemRepository menuItemRepository;
    public CartMapper(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public CartItem cartItemRequestToCartItem(CartItemRequest cartItemRequest){
        CartItem cartItem = new CartItem();
        MenuItem menuItem = menuItemRepository.findById(cartItemRequest.getMenuItemId())
                .orElseThrow(() -> new MenuItemNotFoundException("menuItem not found"));
        cartItem.setMenuItemId(cartItemRequest.getMenuItemId());
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setName(menuItem.getName());
        cartItem.setPrice(menuItem.getPrice());
        Restaurant restaurant = menuItem.getRestaurant();
        cartItem.setRestaurantId(restaurant.getId());
        return cartItem;
    }
}
