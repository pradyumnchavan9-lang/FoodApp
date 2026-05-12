package com.foodapp.food_delivery.repository;

import com.foodapp.food_delivery.model.Order;
import com.foodapp.food_delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

     List<Order> findByUser(User user);
}
