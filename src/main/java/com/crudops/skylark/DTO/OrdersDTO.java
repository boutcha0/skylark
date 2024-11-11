package com.crudops.skylark.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OrdersDTO {
    private Long id;
    private Long customerId; // Referencing the `Info` model's `id`
    private LocalDate orderDate;
    private String status;
    private Double amount;
}
