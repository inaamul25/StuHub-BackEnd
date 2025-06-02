package com.example.demo.dto;


public class PaymentRequestDTO {
    private Integer amount;

    public PaymentRequestDTO() {}

    public PaymentRequestDTO(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

