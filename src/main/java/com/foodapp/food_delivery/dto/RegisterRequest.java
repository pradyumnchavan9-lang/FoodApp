package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
    private String name;
    private Role role;
}
