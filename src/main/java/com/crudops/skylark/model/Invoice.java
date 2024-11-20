package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @Column(name = "total_amount")
    private Double totalAmount;


}