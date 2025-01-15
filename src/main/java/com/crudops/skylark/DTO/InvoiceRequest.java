package com.crudops.skylark.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class InvoiceRequest {

    private String customerId;
    private BigDecimal totalPrice;
    private List<OrderItemDTO> orderItems;
    private ShippingAddressDTO shippingAddress;


}
