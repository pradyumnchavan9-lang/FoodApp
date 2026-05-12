package com.foodapp.food_delivery.model;

import com.foodapp.food_delivery.enums.OrderStatus;
import com.foodapp.food_delivery.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
    private LocalDateTime placedAt;
    private LocalDateTime deliveredAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_partner_id")
    private User deliveryPartner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id",  nullable = false)
    private Restaurant restaurant;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @Column(nullable = false)
    private Double totalPrice;

    @PrePersist
    public void placedAt() {
        this.placedAt = LocalDateTime.now();
    }


}
