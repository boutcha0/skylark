package com.crudops.skylark.DTO;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
}