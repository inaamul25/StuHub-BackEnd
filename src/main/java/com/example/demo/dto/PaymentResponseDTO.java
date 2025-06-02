package com.example.demo.dto;

public class PaymentResponseDTO {

    private String orderId;
    private String key;
    private int amount;
    private String currency;

    // ✅ Constructor with all fields
    public PaymentResponseDTO(String orderId, String key, int amount, String currency) {
        this.orderId = orderId;
        this.key = key;
        this.amount = amount;
        this.currency = currency;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
