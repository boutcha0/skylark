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
    private String productName;
    private String productImage;

    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;

    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (quantity != null && unitPrice != null) {
            this.totalAmount = quantity * unitPrice;
        }
        if (product != null) {
            this.productName = product.getName();
            this.productImage = product.getImage();
        }
    }
}