package com.crudops.skylark.service;

import com.crudops.skylark.DTO.InvoiceDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
    InvoiceDTO getInvoiceById(Long id);
    List<InvoiceDTO> getAllInvoices();
    InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO);
    void deleteInvoice(Long id);
}
