package com.example.demo.service;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.dto.PaymentResponseDTO;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponseDTO createOrder(PaymentRequestDTO request) throws RazorpayException {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RazorpayException("Invalid amount provided");
        }

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", request.getAmount() * 100); // Razorpay expects amount in paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        com.razorpay.Order order = client.orders.create(options);

        return new PaymentResponseDTO(
            order.get("id"),
            keyId,
            order.get("amount"),
            order.get("currency")
        );
    }

    @Override
    public void savePaymentDetails(String orderId, String paymentId, String signature) throws Exception {
        // Verify payment signature
        if (!verifySignature(orderId, paymentId, signature)) {
            throw new Exception("Invalid payment signature");
        }

        // Save payment to database
        Payment payment = new Payment();
        payment.setRazorpayOrderId(orderId);
        payment.setRazorpayPaymentId(paymentId);
        payment.setRazorpaySignature(signature);
        payment.setAmount(0); // Optional: Set actual amount if needed
        payment.setStatus("SUCCESS");
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    private boolean verifySignature(String orderId, String paymentId, String signature) throws Exception {
        String payload = orderId + "|" + paymentId;

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(payload.getBytes());

        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) hash.append('0');
            hash.append(hex);
        }

        return hash.toString().equals(signature);
    }
}
