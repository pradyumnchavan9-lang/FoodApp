package com.foodapp.food_delivery.service;

import com.foodapp.food_delivery.dto.AddressResponse;
import com.foodapp.food_delivery.dto.OwnerResponse;
import com.foodapp.food_delivery.dto.RestaurantRequest;
import com.foodapp.food_delivery.dto.RestaurantResponse;
import com.foodapp.food_delivery.enums.Role;
import com.foodapp.food_delivery.exception.RestaurantNotFoundException;
import com.foodapp.food_delivery.exception.UnauthorizedException;
import com.foodapp.food_delivery.mapper.AddressMapper;
import com.foodapp.food_delivery.mapper.RestaurantMapper;
import com.foodapp.food_delivery.mapper.UserMapper;
import com.foodapp.food_delivery.model.Address;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.model.User;
import com.foodapp.food_delivery.repository.AddressRepository;
import com.foodapp.food_delivery.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final AuthService authService;
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;

    public RestaurantService(AuthService authService, RestaurantRepository restaurantRepository,
                             AddressRepository addressRepository, AddressMapper addressMapper,
                             RestaurantMapper restaurantMapper, UserMapper userMapper) {
        this.authService = authService;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.restaurantMapper = restaurantMapper;
        this.userMapper = userMapper;

    }

    //build Restaurant Response


    //create a restaurant
    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        Restaurant restaurant = restaurantMapper.restaurantRequestToRestaurant(restaurantRequest);

        Address address = addressMapper.restaurantRequestToAddress(restaurantRequest);
        addressRepository.save(address);
        restaurant.setAddress(address);

        User user = authService.getLoggedInUser();
        restaurant.setUser(user);
        restaurantRepository.save(restaurant);

        return restaurantMapper.restaurantToRestaurantResponse(restaurant, user, address);
    }

    //get restaurant by id
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        return restaurantMapper.restaurantToRestaurantResponse(restaurant,restaurant.getUser(), restaurant.getAddress());
    }

    //Convert Restaurants to RestaurantsResponses
    public List<RestaurantResponse> convertRestaurants(List<Restaurant> restaurants) {
        List<RestaurantResponse> myRestaurantsResponses = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantResponse restaurantResponse = restaurantMapper.restaurantToRestaurantResponse(restaurant,restaurant.getUser(), restaurant.getAddress());
            myRestaurantsResponses.add(restaurantResponse);
        }
        return myRestaurantsResponses;
    }

    //Get restaurants of a user
    public List<RestaurantResponse> getMyRestaurants() {
        User user = authService.getLoggedInUser();
        List<Restaurant> myRestaurants = restaurantRepository.findAllByUser(user);
        return convertRestaurants(myRestaurants);
    }

    //toggle restaurant status
    public RestaurantResponse toggleStatus(Long id){
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        User user = authService.getLoggedInUser();
        if (user.getRole().equals(Role.RESTAURANT_OWNER) && user.equals(restaurant.getUser())) {
            restaurant.setIsOpen(!restaurant.getIsOpen());
            restaurantRepository.save(restaurant);
        }else{
            throw new UnauthorizedException("You are not the owner of this restaurant");
        }

        return restaurantMapper.restaurantToRestaurantResponse(restaurant, restaurant.getUser(), restaurant.getAddress());
    }

    //get all restaurants
    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return convertRestaurants(restaurants);
    }

    //find nearby restaurants
    public List<RestaurantResponse> getNearbyRestaurants(double lat, double lon, double radius) {
        return convertRestaurants(restaurantRepository.findNearbyRestaurants(lat, lon, radius));
    }
}
