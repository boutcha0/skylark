package com.crudops.skylark.repository;

import com.crudops.skylark.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}