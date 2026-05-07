package com.foodapp.food_delivery.repository;

import com.foodapp.food_delivery.enums.CuisineType;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByUser(User user);
    List<Restaurant> findAllByIsOpen(Boolean isOpen);
    List<Restaurant> findAllByCuisineType(CuisineType cuisineType);
}
