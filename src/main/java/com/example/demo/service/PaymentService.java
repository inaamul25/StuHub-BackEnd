package com.example.demo.service;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.dto.PaymentResponseDTO;
import com.razorpay.RazorpayException;

public interface PaymentService {
    PaymentResponseDTO createOrder(PaymentRequestDTO request) throws RazorpayException;
    void savePaymentDetails(String orderId, String paymentId, String signature) throws Exception;  // <--- add throws Exception here
}
