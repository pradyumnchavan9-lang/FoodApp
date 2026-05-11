package com.foodapp.food_delivery.exception;

public class QuantityOutOfBoundsException extends RuntimeException {
    public QuantityOutOfBoundsException(String message) {
        super(message);
    }
}
