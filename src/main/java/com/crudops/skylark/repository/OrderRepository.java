package com.crudops.skylark.repository;

import com.crudops.skylark.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByInfoId(Long infoId);

    @Query("SELECT o FROM Order o WHERE o.info.id = :infoId " +
            "AND (:orderId IS NULL OR CAST(o.id AS string) = :orderId) " +
            "AND (:startDate IS NULL OR o.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.orderDate <= :endDate) " +
            "ORDER BY o.orderDate DESC")
    List<Order> findFilteredOrders(
            @Param("infoId") Long infoId,
            @Param("orderId") String orderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}