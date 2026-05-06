package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.Role;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String name;
    private String email;
    private Role role;

}
