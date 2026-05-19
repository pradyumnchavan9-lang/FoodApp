package com.foodapp.food_delivery.repository;

import com.foodapp.food_delivery.enums.MenuCategory;
import com.foodapp.food_delivery.model.MenuItem;
import com.foodapp.food_delivery.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Optional<MenuItem> findById(Long id);
    List<MenuItem> findAllByRestaurantAndCategory(Restaurant restaurant, MenuCategory category);
    List<MenuItem> findAllByRestaurant(Restaurant restaurant);
    List<MenuItem> findByNameContainingIgnoreCase(String searchText);
}
