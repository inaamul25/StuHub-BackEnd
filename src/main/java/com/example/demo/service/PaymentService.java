package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private RazorpayClient razorpayClient;

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostConstruct
    public void init() throws RazorpayException {
        // Initialize Razorpay client with your live keys here
        this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }

    // Create Razorpay Order & save Payment record in DB
    public String createPaymentOrder(Long userId, Long courseId, Double amount) throws RazorpayException {
        if (amount <= 0) {
            throw new RazorpayException("Amount must be greater than 0 for payment processing");
        }

        JSONObject orderRequest = new JSONObject();
        long amountInPaise = (long) (amount * 100); // Razorpay requires amount in paise
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_" + courseId);
        orderRequest.put("payment_capture", 1); // Auto capture payment

        Order order = razorpayClient.orders.create(orderRequest);

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setCourseId(courseId);
        payment.setAmount(amount);
        payment.setTransactionId(order.get("id").toString()); // Store Razorpay Order ID
        payment.setStatus("created");
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return order.get("id").toString(); // Return order ID to frontend
    }

    // Verify payment using Razorpay Payment ID
    public boolean verifyPayment(String razorpayPaymentId) throws RazorpayException {
        com.razorpay.Payment razorpayPayment = razorpayClient.payments.fetch(razorpayPaymentId);

        if ("captured".equals(razorpayPayment.get("status"))) {
            String razorpayOrderId = razorpayPayment.get("order_id").toString();

            Payment dbPayment = paymentRepository.findByTransactionId(razorpayOrderId);

            if (dbPayment != null) {
                dbPayment.setStatus("completed");
                paymentRepository.save(dbPayment);
                return true;
            }
        }
        return false;
    }

    // Update payment status manually if needed
    public void updatePaymentStatus(String transactionId, String status) {
        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.save(payment);
        }
    }
}
