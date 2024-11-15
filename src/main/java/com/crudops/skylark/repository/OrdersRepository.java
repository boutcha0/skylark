package com.crudops.skylark.repository;

import com.crudops.skylark.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdAndOrderDate(Long customerId, LocalDate orderDate);

    Order findByCustomerId(Long customerId);
}
