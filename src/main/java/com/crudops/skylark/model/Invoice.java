package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, unique = true)
    private Order order;

    private LocalDate invoiceDate;
    private Double totalAmount;
    private String billingAddress;
    private String status;  // e.g., "Paid", "Unpaid", "Pending"

    // Custom equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) &&
                Objects.equals(order, invoice.order) &&
                Objects.equals(invoiceDate, invoice.invoiceDate) &&
                Objects.equals(totalAmount, invoice.totalAmount) &&
                Objects.equals(billingAddress, invoice.billingAddress) &&
                Objects.equals(status, invoice.status);
    }

    // Custom hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, order, invoiceDate, totalAmount, billingAddress, status);
    }
}
