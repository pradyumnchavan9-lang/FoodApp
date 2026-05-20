package com.foodapp.food_delivery.controller;

import com.foodapp.food_delivery.dto.RestaurantRequest;
import com.foodapp.food_delivery.dto.RestaurantResponse;
import com.foodapp.food_delivery.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping()
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        return new ResponseEntity<>(restaurantService.createRestaurant(restaurantRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id){
        return new ResponseEntity<>(restaurantService.getRestaurantById(id), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<RestaurantResponse>> getMyRestaurants(){
        return new ResponseEntity<>(restaurantService.getMyRestaurants(), HttpStatus.OK);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<RestaurantResponse> toggleStatus(@PathVariable Long id){
        return new ResponseEntity<>(restaurantService.toggleStatus(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(){
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantResponse>> getNearbyRestaurants(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        return new ResponseEntity<>(restaurantService.getNearbyRestaurants(lat, lon, radius), HttpStatus.OK);
    }
}
