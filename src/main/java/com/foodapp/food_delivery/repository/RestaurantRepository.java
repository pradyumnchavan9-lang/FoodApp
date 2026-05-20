package com.foodapp.food_delivery.repository;

import com.foodapp.food_delivery.enums.CuisineType;
import com.foodapp.food_delivery.model.Restaurant;
import com.foodapp.food_delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByUser(User user);
    List<Restaurant> findAllByIsOpen(Boolean isOpen);
    List<Restaurant> findAllByCuisineType(CuisineType cuisineType);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    @Query(value =
            """
                SELECT r.* FROM restaurants r\s
                JOIN addresses a ON r.address_id = a.id
                WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) *\s
                cos(radians(a.longitude) - radians(:lon)) +\s
                sin(radians(:lat)) * sin(radians(a.latitude)))) <= :radius"""
    , nativeQuery = true)
    List<Restaurant> findNearbyRestaurants(@Param("lat") double lat, @Param("lon") double lon, @Param("radius") double radius);
}
