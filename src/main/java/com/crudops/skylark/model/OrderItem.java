package com.crudops.skylark.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Double totalPrice;

    // Constructor to calculate prices automatically
    public OrderItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.totalPrice = calculateTotalPrice();
    }

    // Default constructor with explicit total price initialization
    public OrderItem(Product product, Integer quantity, Double unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice();
    }

    // Method to calculate total price
    public Double calculateTotalPrice() {
        return this.unitPrice * this.quantity;
    }

    // Update price when quantity changes
    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        this.totalPrice = calculateTotalPrice();
    }

    // Prepersist method to ensure total price is set before saving
    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.totalPrice == null) {
            this.totalPrice = calculateTotalPrice();
        }
        if (this.unitPrice == null && this.product != null) {
            this.unitPrice = this.product.getPrice();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) &&
                Objects.equals(product.getId(), orderItem.product.getId()) &&
                Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product.getId(), quantity);
    }
}