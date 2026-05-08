package com.foodapp.food_delivery.mapper;

import com.foodapp.food_delivery.dto.OwnerResponse;
import com.foodapp.food_delivery.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public OwnerResponse userToOwnerResponse(User user) {
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setEmail(user.getEmail());
        ownerResponse.setName(user.getName());
        return ownerResponse;
    }
}
