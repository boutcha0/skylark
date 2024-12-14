package com.crudops.skylark.DTO;

import lombok.Data;

@Data
public class PaymentRequest {
    private double amount;
    private String currency = "usd";
}