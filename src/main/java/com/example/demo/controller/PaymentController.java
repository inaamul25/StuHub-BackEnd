package com.example.demo.controller;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.dto.PaymentResponseDTO;
import com.example.demo.service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create Razorpay order
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            PaymentResponseDTO responseDTO = paymentService.createOrder(paymentRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    // Verify payment and save details
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> paymentDetails) {
        try {
            paymentService.savePaymentDetails(
                    paymentDetails.get("razorpayOrderId"),
                    paymentDetails.get("razorpayPaymentId"),
                    paymentDetails.get("razorpaySignature")
            );
            return ResponseEntity.ok("Payment verified and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Payment verification failed: " + e.getMessage());
        }
    }
}
