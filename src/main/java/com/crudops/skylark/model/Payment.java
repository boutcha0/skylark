package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, unique = true)
    private Order order;

    private Double paymentAmount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String status;

    // Custom equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
                Objects.equals(paymentAmount, payment.paymentAmount) &&
                Objects.equals(paymentDate, payment.paymentDate) &&
                Objects.equals(paymentMethod, payment.paymentMethod) &&
                Objects.equals(status, payment.status);
    }

    // Custom hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, paymentAmount, paymentDate, paymentMethod, status);
    }
}
