package com.crudops.skylark.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class InvoiceRequest {
    // Getters and Setters
    private String customerId;
    private BigDecimal totalPrice;

    // Constructors
    public InvoiceRequest() {}

    public InvoiceRequest(String customerId, BigDecimal totalPrice) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
    }

}
