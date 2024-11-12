package com.crudops.skylark.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long id;
    private Long orderId; // Should match `Order` entity's ID
    private Double paymentAmount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String status;
}
