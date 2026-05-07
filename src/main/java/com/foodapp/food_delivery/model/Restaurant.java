package com.foodapp.food_delivery.model;

import com.foodapp.food_delivery.enums.CuisineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @Column(nullable = false)
    private Boolean isOpen = false;
    private Double rating;

}
