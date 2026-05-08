package com.foodapp.food_delivery.mapper;

import com.foodapp.food_delivery.dto.AddressResponse;
import com.foodapp.food_delivery.dto.OwnerResponse;
import com.foodapp.food_delivery.dto.RestaurantRequest;
import com.foodapp.food_delivery.dto.RestaurantResponse;
import com.foodapp.food_delivery.model.Address;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.model.User;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public RestaurantMapper(UserMapper userMapper, AddressMapper addressMapper) {
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }

    public RestaurantResponse restaurantToRestaurantResponse(Restaurant restaurant, User user, Address address) {
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        restaurantResponse.setId(restaurant.getId());
        restaurantResponse.setName(restaurant.getName());
        restaurantResponse.setDescription(restaurant.getDescription());
        restaurantResponse.setIsOpen(restaurant.getIsOpen());
        restaurantResponse.setRating(restaurant.getRating());
        restaurantResponse.setPhone(restaurant.getPhone());
        restaurantResponse.setCuisineType(restaurant.getCuisineType());
        AddressResponse addressResponse = addressMapper.addressToAddressResponse(address);
        restaurantResponse.setAddressResponse(addressResponse);

        OwnerResponse ownerResponse = userMapper.userToOwnerResponse(user);
        restaurantResponse.setOwnerResponse(ownerResponse);
        return restaurantResponse;
    }

    public Restaurant restaurantRequestToRestaurant(RestaurantRequest restaurantRequest) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantRequest.getName());
        restaurant.setDescription(restaurantRequest.getDescription());
        restaurant.setPhone(restaurantRequest.getPhone());
        restaurant.setCuisineType(restaurantRequest.getCuisineType());
        restaurant.setRating(7.0);
        return restaurant;
    }
}
