package com.crudops.skylark.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InvoiceDTO {
    private Long id;
    private Long orderId;
    private LocalDate invoiceDate;
    private Double totalAmount;
    private String billingAddress;
    private String status;
}
