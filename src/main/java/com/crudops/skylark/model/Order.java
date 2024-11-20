package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Column(name = "order_date")
    private LocalDate orderDate;

    private String status;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "invoice_id", nullable = true)
    private Long invoiceId; // Links this order to an invoice



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(status, order.status) &&
                Objects.equals(amount, order.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, status, amount);
    }

}