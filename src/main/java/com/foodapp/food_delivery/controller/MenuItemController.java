package com.foodapp.food_delivery.controller;

import com.foodapp.food_delivery.dto.MenuItemRequest;
import com.foodapp.food_delivery.dto.MenuItemResponse;
import com.foodapp.food_delivery.enums.MenuCategory;
import com.foodapp.food_delivery.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/{restaurantId}/menu")
public class MenuItemController{

    private final MenuItemService menuItemService;
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping()
    public ResponseEntity<MenuItemResponse> createMenuItem(@RequestBody MenuItemRequest menuItemRequest, @PathVariable Long restaurantId){
        return new ResponseEntity<>(menuItemService.createMenuItem(menuItemRequest, restaurantId), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long menuItemId){
        menuItemService.deleteMenuItem(menuItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PutMapping("/{menuItemId}/available")
    public ResponseEntity<MenuItemResponse> updateMenuItem(@PathVariable Long menuItemId){
        return new ResponseEntity<>(menuItemService.updateAvailability(menuItemId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<MenuItemResponse>> getAllByRestaurantAndCategory(@PathVariable Long restaurantId, @RequestParam(required = false) MenuCategory category){
        if(category == null) {
            return new ResponseEntity<>(menuItemService.getMenuByRestaurant(restaurantId), HttpStatus.OK);
        }
        return new ResponseEntity<>(menuItemService.getAllByRestaurantAndCategory(restaurantId, category), HttpStatus.OK);
    }

}
