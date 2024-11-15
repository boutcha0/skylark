package com.crudops.skylark.repository;

import com.crudops.skylark.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByInvoiceDate(LocalDate invoiceDate);
}
