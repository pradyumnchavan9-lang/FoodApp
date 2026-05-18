package com.foodapp.food_delivery.service;

import com.foodapp.food_delivery.dto.MenuItemRequest;
import com.foodapp.food_delivery.dto.MenuItemResponse;
import com.foodapp.food_delivery.dto.RestaurantResponse;
import com.foodapp.food_delivery.enums.MenuCategory;
import com.foodapp.food_delivery.exception.MenuItemNotFoundException;
import com.foodapp.food_delivery.exception.RestaurantNotFoundException;
import com.foodapp.food_delivery.exception.UnauthorizedException;
import com.foodapp.food_delivery.mapper.MenuItemMapper;
import com.foodapp.food_delivery.mapper.RestaurantMapper;
import com.foodapp.food_delivery.model.MenuItem;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.model.User;
import com.foodapp.food_delivery.repository.MenuItemRepository;
import com.foodapp.food_delivery.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {


    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;
    private final AuthService authService;
    private final MenuItemRepository menuItemRepository;
    private final RedisService  redisService;
    public MenuItemService(RestaurantRepository restaurantRepository, AuthService authService,
                           MenuItemMapper menuItemMapper, MenuItemRepository menuItemRepository,
                           RedisService redisService) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
        this.authService = authService;
        this.menuItemRepository = menuItemRepository;
        this.redisService = redisService;
    }

    //create item or add item
    public MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        User user = authService.getLoggedInUser();
        if(user.equals(restaurant.getUser())) {
            MenuItem menuItem = menuItemMapper.menuItemRequestToMenuItem(menuItemRequest, restaurant);
            redisService.deleteByPattern(menuItem.getRestaurant().getId() + ":menu*");
            menuItemRepository.save(menuItem);
            return menuItemMapper.menuItemToMenuItemResponse(menuItem, restaurant);
        }else{
            throw new UnauthorizedException("Only Owner can add Item");
        }
    }

    //delete item
    public void deleteMenuItem(Long menuItemId){
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                        .orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found"));
        User user = authService.getLoggedInUser();
        if(user.equals(menuItem.getRestaurant().getUser())) {
            redisService.deleteByPattern(menuItem.getRestaurant().getId() + ":menu*");
            menuItemRepository.deleteById(menuItemId);
        }else{
            throw new UnauthorizedException("Only Owner can delete Item");
        }
    }

    //update availability
    public MenuItemResponse updateAvailability(Long menuItemId){

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item is unavailable"));
        User user =  authService.getLoggedInUser();
        if(user.equals(menuItem.getRestaurant().getUser())){
            menuItem.setIsAvailable(!menuItem.getIsAvailable());
            menuItemRepository.save(menuItem);
            redisService.deleteByPattern(menuItem.getRestaurant().getId() + ":menu*");
            return menuItemMapper.menuItemToMenuItemResponse(menuItem, menuItem.getRestaurant());
        }else{
            throw new UnauthorizedException("Only Owner can update Item availability");
        }
    }

    //get all by category
    public List<MenuItemResponse> getAllByRestaurantAndCategory(Long restaurantId, MenuCategory category) {
        String key = restaurantId + ":menu:" + category + ":";
        List<MenuItemResponse> cache = redisService.get(key, List.class);
        if(cache != null){
            return cache;
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantAndCategory(restaurant, category);
        List<MenuItemResponse> responseList = convertToMenuItemResponseList(menuItems);
        redisService.set(key, responseList, 300);
        return responseList;
    }

    //get Menu By restaurant
    public List<MenuItemResponse> getMenuByRestaurant(Long restaurantId) {
        String key = restaurantId + ":menu:";
        List<MenuItemResponse> cache = redisService.get(key, List.class);
        if(cache != null){
            return cache;
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        List<MenuItem> menuItems = menuItemRepository.findAllByRestaurant(restaurant);
        List<MenuItemResponse> responseList = convertToMenuItemResponseList(menuItems);
        redisService.set(key, responseList, 300);
        return responseList;
    }

    //convert list of menuItems to list of menu Items responses
    public List<MenuItemResponse> convertToMenuItemResponseList(List<MenuItem> menuItems) {

        List<MenuItemResponse> menuItemResponseList = new ArrayList<>();
        for(MenuItem menuItem : menuItems){
            MenuItemResponse menuItemResponse = menuItemMapper.menuItemToMenuItemResponse(menuItem, menuItem.getRestaurant());
            menuItemResponseList.add(menuItemResponse);
        }
        return menuItemResponseList;
    }
}
