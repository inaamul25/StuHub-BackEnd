package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create Razorpay order
    @PostMapping("/create-order")
    public ResponseEntity<?> createPaymentOrder(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam Double amount) {
        try {
            String orderId = paymentService.createPaymentOrder(userId, courseId, amount);
            return ResponseEntity.ok(Map.of("orderId", orderId, "amount", amount));
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    // Update payment status (optional, can be used if you want manual update)
    @PostMapping("/update-status")
    public ResponseEntity<?> updatePaymentStatus(
            @RequestParam String transactionId,
            @RequestParam String status) {
        try {
            paymentService.updatePaymentStatus(transactionId, status);
            return ResponseEntity.ok("Payment status updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Verify payment after payment success
    @PostMapping("/verify-payment")
    public ResponseEntity<Boolean> verifyPayment(@RequestBody Map<String, String> payload) {
        String paymentId = payload.get("paymentId");
        try {
            boolean isVerified = paymentService.verifyPayment(paymentId);
            return ResponseEntity.ok(isVerified);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body(false);
        }
    }
}
