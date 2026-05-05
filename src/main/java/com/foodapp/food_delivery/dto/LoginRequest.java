package com.foodapp.food_delivery.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
