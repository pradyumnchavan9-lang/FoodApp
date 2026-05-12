package com.foodapp.food_delivery.service;

import com.foodapp.food_delivery.dto.OrderResponse;
import com.foodapp.food_delivery.dto.UpdateOrderStatusRequest;
import com.foodapp.food_delivery.enums.OrderStatus;
import com.foodapp.food_delivery.enums.PaymentStatus;
import com.foodapp.food_delivery.enums.Role;
import com.foodapp.food_delivery.exception.*;
import com.foodapp.food_delivery.mapper.OrderMapper;
import com.foodapp.food_delivery.model.*;
import com.foodapp.food_delivery.repository.MenuItemRepository;
import com.foodapp.food_delivery.repository.OrderRepository;
import com.foodapp.food_delivery.repository.RestaurantRepository;
import com.foodapp.food_delivery.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final AuthService authService;
    private final RedisService redisService;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    public  OrderService(AuthService authService, RedisService redisService,
                         RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository,
                         OrderRepository orderRepository, OrderMapper orderMapper,
                         UserRepository userRepository) {
        this.authService = authService;
        this.redisService = redisService;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse checkout(){

        //Fetch user for getting his cart
        User user = authService.getLoggedInUser();

        //generate key to get user's cart
        String key = "cart:" + user.getId();
        Cart cart = redisService.get(key, Cart.class);

        if(cart == null || cart.getCartItems().isEmpty()){
            throw new CartNotFoundException("Your cart is empty");
        }

        //convert cart into order
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setUser(user);
        Restaurant restaurant = restaurantRepository.findById(cart.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        order.setRestaurant(restaurant);
        List<OrderItem> orderItems = convertCartItemsToOrderItems(cart.getCartItems(), order);
        order.setOrderItems(orderItems);
        order.setTotalPrice(cart.calculateTotalPrice());
        orderRepository.save(order);
        redisService.delete(key);
        return orderMapper.orderToOrderResponse(order);

    }

    private List<OrderItem> convertCartItemsToOrderItems(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItemRepository.findById(cartItem.getMenuItemId())
                    .orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found")));
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setName(cartItem.getName());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    //get order by id
    public OrderResponse getOrder(Long orderId){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return orderMapper.orderToOrderResponse(order);
    }

    //convert order list to order response list
    public List<OrderResponse> convertOrderListToOrderResponseList(List<Order> orders){

        List<OrderResponse> orderResponseList = new ArrayList<>();
        for(Order order : orders){
            orderResponseList.add(orderMapper.orderToOrderResponse(order));
        }
        return orderResponseList;
    }

    //get your orders
    public List<OrderResponse> getMyOrders(){

        User  user = authService.getLoggedInUser();
        List<Order> orders = orderRepository.findByUser(user);
        return convertOrderListToOrderResponseList(orders);
    }

    //update order status
    public OrderResponse updateOrderStatus(UpdateOrderStatusRequest updateOrderStatusRequest){

        User user = authService.getLoggedInUser();

        Order order = orderRepository.findById(updateOrderStatusRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderStatus orderStatus = updateOrderStatusRequest.getOrderStatus();
        switch(orderStatus){
            case ACCEPTED:
                if(user.getRole().equals(Role.RESTAURANT_OWNER)){
                    order.setOrderStatus(OrderStatus.ACCEPTED);
                }else{
                    throw new UnauthorizedException("Only restaurant owner can accept or reject order");
                }
                break;
            case REJECTED:
                if(user.getRole().equals(Role.RESTAURANT_OWNER)){
                    order.setOrderStatus(OrderStatus.REJECTED);
                }else{
                    throw new UnauthorizedException("Only restaurant owner can reject order");
                }
                break;
            case PREPARING:
                if(user.getRole().equals(Role.RESTAURANT_OWNER)){
                    order.setOrderStatus(OrderStatus.PREPARING);
                }else{
                    throw new UnauthorizedException("Only restaurant owner can prepare order");
                }
                break;
            case PICKED:
                if(user.getRole().equals(Role.DELIVERY_PARTNER)){
                    order.setOrderStatus(OrderStatus.PICKED);
                }else {
                    throw new UnauthorizedException("Only delivery partner can pick order");
                }
                break;
            case DELIVERED:
                if(user.getRole().equals(Role.DELIVERY_PARTNER)){
                    order.setOrderStatus(OrderStatus.DELIVERED);
                    order.setDeliveredAt(LocalDateTime.now());
                }else {
                    throw new UnauthorizedException("Only delivery partner can deliver order");
                }
                break;
            case CANCELLED:
                if(user.getRole().equals(Role.CUSTOMER)){
                    order.setOrderStatus(OrderStatus.CANCELLED);
                }else {
                    throw new UnauthorizedException("Only customer can cancel order");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown order status");
        }
        orderRepository.save(order);
        return orderMapper.orderToOrderResponse(order);
    }
}
