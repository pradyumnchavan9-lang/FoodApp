package com.foodapp.food_delivery.mapper;

import com.foodapp.food_delivery.dto.MenuItemRequest;
import com.foodapp.food_delivery.dto.MenuItemResponse;
import com.foodapp.food_delivery.dto.RestaurantResponse;
import com.foodapp.food_delivery.exception.RestaurantNotFoundException;
import com.foodapp.food_delivery.model.MenuItem;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    public MenuItemMapper(RestaurantRepository restaurantRepository,  RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    public MenuItem menuItemRequestToMenuItem(MenuItemRequest menuItemRequest, Restaurant restaurant) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemRequest.getName());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setCategory(menuItemRequest.getCategory());
        menuItem.setIsVeg(menuItemRequest.getIsVeg());
        menuItem.setServes(menuItemRequest.getServes());
        menuItem.setPortionSize(menuItemRequest.getPortionSize());
        menuItem.setIsAvailable(menuItemRequest.getIsAvailable());
        menuItem.setRestaurant(restaurant);
        return menuItem;
    }

    public MenuItemResponse menuItemToMenuItemResponse(MenuItem menuItem, Restaurant restaurant) {
        MenuItemResponse menuItemResponse = new MenuItemResponse();
        menuItemResponse.setId(menuItem.getId());
        menuItemResponse.setName(menuItem.getName());
        menuItemResponse.setPrice(menuItem.getPrice());
        menuItemResponse.setCategory(menuItem.getCategory());
        menuItemResponse.setIsAvailable(menuItem.getIsAvailable());
        menuItemResponse.setIsVeg(menuItem.getIsVeg());
        RestaurantResponse restaurantResponse = restaurantMapper.restaurantToRestaurantResponse(restaurant, restaurant.getUser(), restaurant.getAddress());
        menuItemResponse.setRestaurantId(restaurantResponse.getId());
        return menuItemResponse;
    }
}
