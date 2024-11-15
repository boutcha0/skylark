package com.crudops.skylark.service;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    List<Order> getOrdersByCustomerId(Long customerId);

    InvoiceDTO generateInvoice(Long customerId, LocalDate invoiceDate);

}
