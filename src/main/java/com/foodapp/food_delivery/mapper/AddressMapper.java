package com.foodapp.food_delivery.mapper;

import com.foodapp.food_delivery.dto.AddressResponse;
import com.foodapp.food_delivery.dto.RestaurantRequest;
import com.foodapp.food_delivery.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse addressToAddressResponse(Address address) {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(address.getId());
        addressResponse.setCity(address.getCity());
        addressResponse.setState(address.getState());
        addressResponse.setStreet(address.getStreet());
        addressResponse.setZip(address.getZip());
        addressResponse.setLatitude(address.getLatitude());
        addressResponse.setLongitude(address.getLongitude());
        return addressResponse;
    }

    public Address restaurantRequestToAddress(RestaurantRequest restaurantRequest) {
        Address address = new Address();
        address.setCity(restaurantRequest.getAddress().getCity());
        address.setState(restaurantRequest.getAddress().getState());
        address.setStreet(restaurantRequest.getAddress().getStreet());
        address.setZip(restaurantRequest.getAddress().getZip());
        address.setLatitude(restaurantRequest.getAddress().getLatitude());
        address.setLongitude(restaurantRequest.getAddress().getLongitude());
        return address;

    }
}
