package com.example.demo.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId; // Payment gateway transaction ID (e.g., Stripe PaymentIntent ID)

    @Column(nullable = false)
    private Long userId; // Reference to the User entity (could be a @ManyToOne relationship if preferred)

    @Column(nullable = false)
    private Long courseId; // Reference to the Course entity (could be a @ManyToOne relationship if preferred)

    @Column(nullable = false)
    private Double amount; // Amount in rupees (e.g., 0.0 for free courses)

    @Column(nullable = false)
    private String status; // e.g., "pending", "completed", "failed"

    @Column(nullable = false)
    private LocalDateTime createdAt; // Timestamp of payment creation

    // Default constructor (required by JPA)
    public Payment() {
        this.createdAt = LocalDateTime.now(); // Set default creation time
    }

    // Parameterized constructor (optional, for convenience)
    public Payment(String transactionId, Long userId, Long courseId, Double amount, String status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.courseId = courseId;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // toString (optional, for debugging)
    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}