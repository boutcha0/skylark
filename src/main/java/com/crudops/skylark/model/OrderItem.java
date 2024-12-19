// OrderItem.java
package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice; // Added this field to match database schema

    // Helper method to calculate total price
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (quantity != null && unitPrice != null) {
            this.totalAmount = quantity * unitPrice;
            this.totalPrice = this.totalAmount; // Set total_price same as totalAmount
        }
    }
}