package com.crudops.skylark.service;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    void generateInvoicesForCompletedOrders();

    List<InvoiceDTO> getInvoices();

    byte[] generateInvoicePdf(Long invoiceId) throws Exception;


}