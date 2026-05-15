package com.foodapp.food_delivery.controller;

import com.foodapp.food_delivery.dto.PaymentResponse;
import com.foodapp.food_delivery.dto.PaymentVerificationRequest;
import com.foodapp.food_delivery.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create/{orderId}")
    public ResponseEntity<PaymentResponse> createPayment(Long orderId){
        return new ResponseEntity<>(paymentService.createPayment(orderId), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody PaymentVerificationRequest paymentVerificationRequest){
        return new ResponseEntity<>(paymentService.verifyPayment(paymentVerificationRequest), HttpStatus.OK);
    }
}
