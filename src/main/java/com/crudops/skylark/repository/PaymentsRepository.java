package com.crudops.skylark.repository;

import com.crudops.skylark.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {
}
