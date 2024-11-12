package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.model.Order;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceDTO toDto(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setOrderId(invoice.getOrder().getId());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setBillingAddress(invoice.getBillingAddress());
        dto.setStatus(invoice.getStatus());
        return dto;
    }

    public Invoice toEntity(InvoiceDTO dto, Order order) {
        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setOrder(order);
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setBillingAddress(dto.getBillingAddress());
        invoice.setStatus(dto.getStatus());
        return invoice;
    }
}
