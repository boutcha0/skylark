package com.crudops.skylark.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class OrdersDTO {
    private Long id;
    private Double amount;
    private LocalDate orderDate;
    private String status;
    private Long customerId;
    private Long invoiceId;

}
