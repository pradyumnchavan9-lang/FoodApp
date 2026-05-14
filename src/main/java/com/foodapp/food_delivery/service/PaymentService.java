package com.foodapp.food_delivery.service;

import com.foodapp.food_delivery.dto.PaymentResponse;
import com.foodapp.food_delivery.dto.PaymentVerificationRequest;
import com.foodapp.food_delivery.enums.PaymentStatus;
import com.foodapp.food_delivery.enums.PaymentTransactionStatus;
import com.foodapp.food_delivery.exception.OrderNotFoundException;
import com.foodapp.food_delivery.exception.PaymentFailedException;
import com.foodapp.food_delivery.exception.PaymentNotFoundException;
import com.foodapp.food_delivery.model.Order;
import com.foodapp.food_delivery.model.Payment;
import com.foodapp.food_delivery.repository.OrderRepository;
import com.foodapp.food_delivery.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public PaymentService(RazorpayClient razorpayClient, OrderRepository orderRepository,
                          PaymentRepository paymentRepository, AuthService authService) {
        this.razorpayClient = razorpayClient;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    //create payment
    public PaymentResponse createPayment(Long orderId){

        //find the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        //build the razorpay order request which is a json object
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (order.getTotalPrice() * 100));
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_" + orderId);

        //call razorpay
        try {
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalPrice());
            payment.setStatus(PaymentTransactionStatus.INITIATED);
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            paymentRepository.save(payment);
            return paymentToPaymentResponse(payment);

        } catch (RazorpayException e) {
            throw new PaymentFailedException("Payment Initialization failed");
        }

    }

    public PaymentResponse paymentToPaymentResponse(Payment payment){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(payment.getId());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setRazorpayOrderId(payment.getRazorpayOrderId());
        paymentResponse.setCurrency("INR");
        paymentResponse.setRazorpayPaymentId(payment.getRazorpayPaymentId());
        paymentResponse.setStatus(payment.getStatus());
        paymentResponse.setOrderId(payment.getOrder().getId());
        return paymentResponse;
    }

    @Transactional
    public PaymentResponse verifyPayment(PaymentVerificationRequest paymentVerificationRequest)  {

        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", paymentVerificationRequest.getRazorpayOrderId());
        attributes.put("razorpay_payment_id", paymentVerificationRequest.getRazorpayPaymentId());
        attributes.put("razorpay_signature", paymentVerificationRequest.getPaymentSignature());
        Payment payment = paymentRepository.findByRazorpayOrderId(paymentVerificationRequest.getRazorpayOrderId())
                .orElseThrow(() -> new PaymentNotFoundException(""));
        Order order = payment.getOrder();
        try {
            if(Utils.verifyPaymentSignature(attributes, razorpayKeySecret)){
                payment.setRazorpayPaymentId(paymentVerificationRequest.getRazorpayPaymentId());
                payment.setRazorpaySignature(paymentVerificationRequest.getPaymentSignature());
                payment.setStatus(PaymentTransactionStatus.SUCCESS);
                order.setPaymentStatus(PaymentStatus.PAYED);
                orderRepository.save(order);
                paymentRepository.save(payment);
                return paymentToPaymentResponse(payment);

            }else{
                payment.setStatus(PaymentTransactionStatus.FAILED);
                paymentRepository.save(payment);
                throw new PaymentFailedException("Payment Verification Failed");
            }
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }
}
