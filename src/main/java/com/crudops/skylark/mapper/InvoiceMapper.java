package com.crudops.skylark.mapper;

import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.DTO.InvoiceDTO;  // Assuming you have an InvoiceDTO
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceDTO toDto(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceDate(invoice.getInvoiceDate());  // Correct method
        dto.setTotalAmount(invoice.getTotalAmount());
        // You can also map orders if needed, but that will depend on your use case
        return dto;
    }

    public Invoice toEntity(InvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setInvoiceDate(dto.getInvoiceDate());  // Correct method
        invoice.setTotalAmount(dto.getTotalAmount());
        // Add logic to convert DTO's orders if applicable
        return invoice;
    }


}
