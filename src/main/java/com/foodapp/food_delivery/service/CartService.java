package com.foodapp.food_delivery.service;

import com.foodapp.food_delivery.dto.CartItemRequest;
import com.foodapp.food_delivery.exception.CartItemNotFoundException;
import com.foodapp.food_delivery.exception.CartNotFoundException;
import com.foodapp.food_delivery.exception.DifferentRestaurantException;
import com.foodapp.food_delivery.exception.QuantityOutOfBoundsException;
import com.foodapp.food_delivery.mapper.CartMapper;
import com.foodapp.food_delivery.model.Cart;
import com.foodapp.food_delivery.model.CartItem;
import com.foodapp.food_delivery.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartMapper cartMapper;
    private final AuthService authService;
    private final RedisService redisService;
    public CartService(CartMapper cartMapper, AuthService authService,
                       RedisService redisService) {
        this.cartMapper = cartMapper;
        this.authService = authService;
        this.redisService = redisService;
    }

    public Cart addItem(CartItemRequest cartItemRequest){

        CartItem cartItem = cartMapper.cartItemRequestToCartItem(cartItemRequest);
        User user = authService.getLoggedInUser();
        String key = "cart:" + user.getId();
        Cart cart = redisService.get(key, Cart.class);
        if(cart==null){
            cart = new Cart();
            cart.setUserId(user.getId());
            cart.setRestaurantId(cartItem.getRestaurantId());
            List<CartItem> cartItems = new ArrayList<>();
            cartItems.add(cartItem);
            cart.setCartItems(cartItems);
            redisService.set(key, cart, 300);
            return cart;
        }else{
            if(cart.getRestaurantId().equals(cartItem.getRestaurantId())){
                List<CartItem> cartItems = cart.getCartItems();
                CartItem oldCartItem = null;
                for (CartItem cartItem1 : cartItems) {
                    if(cartItem1.getMenuItemId().equals(cartItem.getMenuItemId())){
                        oldCartItem = cartItem1;
                        cartItem.setQuantity(cartItem1.getQuantity() + cartItem.getQuantity());
                    }
                }
                if(oldCartItem != null){
                    cartItems.remove(oldCartItem);
                }
                cartItems.add(cartItem);
                cart.setCartItems(cartItems);
                redisService.set(key, cart, 300);
                return cart;
            }else{
                throw new DifferentRestaurantException("One cart can have only one restaurants items");
            }
        }
    }

    //remove item from cart
    public Cart removeItem(CartItemRequest cartItemRequest){

        //get logged-in user to generate key for redis
        User user = authService.getLoggedInUser();

        //use key to fetch the cart from redis
        String key = "cart:" + user.getId();
        Cart cart = redisService.get(key, Cart.class);

        if(cart == null){
            throw new CartNotFoundException("You have not created a cart yet");
        }

        //Get the items from the cart
        List<CartItem> cartItems = cart.getCartItems();
        CartItem oldCartItem = null;
        //iterate over the items to find the one to remove
        for(CartItem cartItem : cartItems){

            //condition to find the item
            if(cartItem.getMenuItemId().equals(cartItemRequest.getMenuItemId())){
                oldCartItem = cartItem;
                //if quantity not provided remove 1
                if(cartItemRequest.getQuantity() == null){
                    cartItem.setQuantity(cartItem.getQuantity() -1);
                }else{
                    if(cartItem.getQuantity() >= cartItemRequest.getQuantity()){
                        cartItem.setQuantity(cartItem.getQuantity() - cartItemRequest.getQuantity());
                    }else{
                        throw new QuantityOutOfBoundsException("Cannot remove more items than present in cart");
                    }
                }
            }
        }
        if(oldCartItem != null){
            cartItems.remove(oldCartItem);
            if(oldCartItem.getQuantity() > 0){
                cartItems.add(oldCartItem);
            }
        }else{
            throw new CartItemNotFoundException("You cannot remove item which is not added to cart");
        }
        cart.setCartItems(cartItems);
        redisService.set(key, cart, 300);
        return cart;
    }

    //get cart
    public Cart getCart(){
        //get user to generate key for redis
        User user = authService.getLoggedInUser();
        String key = "cart:" + user.getId();

        //get cart using the key
        Cart cart = redisService.get(key, Cart.class);
        if(cart==null){
            throw new CartNotFoundException("You have not created a cart yet");
        }

        return cart;
    }

    //clear cart
    public void clearCart() {
        User user = authService.getLoggedInUser();
        String key = "cart:" + user.getId();
        redisService.delete(key);
    }

}
