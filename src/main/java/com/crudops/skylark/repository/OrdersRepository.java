package com.crudops.skylark.repository;

import com.crudops.skylark.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    // Find completed orders grouped by customerId
    @Query("SELECT o.customerId, SUM(o.amount) FROM Order o WHERE o.status = 'completed' GROUP BY o.customerId")
    List<Object[]> findCompletedOrdersGroupedByCustomer();

    // Update the invoice ID for completed orders by a specific customer
    @Modifying
    @Query("UPDATE Order o SET o.invoiceId = :invoiceId WHERE o.customerId = :customerId AND o.status = 'completed'")
    void updateInvoiceIdForCustomerOrders(@Param("customerId") Long customerId, @Param("invoiceId") Long invoiceId);

    Order findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndOrderDate(Long customerId, LocalDate date);
}
