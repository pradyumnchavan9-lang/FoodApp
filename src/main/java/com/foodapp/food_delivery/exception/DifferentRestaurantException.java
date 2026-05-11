package com.foodapp.food_delivery.exception;

public class DifferentRestaurantException extends RuntimeException {
    public DifferentRestaurantException(String message) {
        super(message);
    }
}
