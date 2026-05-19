package com.foodapp.food_delivery.service;

import com.foodapp.food_delivery.dto.SearchResponse;
import com.foodapp.food_delivery.model.MenuItem;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.repository.MenuItemRepository;
import com.foodapp.food_delivery.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    public SearchService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository,
                         RestaurantService restaurantService, MenuItemService menuItemService) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository ;
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    public SearchResponse search(String searchText) {

        SearchResponse searchResponse = new SearchResponse();
        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(searchText);
        searchResponse.setRestaurants(restaurantService.convertRestaurants(restaurants));
        List<MenuItem> menuItems = menuItemRepository.findByNameContainingIgnoreCase(searchText);
        searchResponse.setMenuItems(menuItemService.convertToMenuItemResponseList(menuItems));
        return searchResponse;
    }
}
