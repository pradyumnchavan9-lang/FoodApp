package com.foodapp.food_delivery.dto;

import com.foodapp.food_delivery.enums.PaymentTransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String razorpayOrderId;
    private Double amount;
    private String currency;
    private Long paymentId;
    private String razorpayPaymentId;
    private PaymentTransactionStatus status;
    private Long orderId;

}
