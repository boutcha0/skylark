package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate invoiceDate;
    private Double totalAmount;
    @Setter
    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
