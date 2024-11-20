package com.crudops.skylark.mapper;

import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.DTO.InvoiceDTO;  // Assuming you have an InvoiceDTO
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    public InvoiceDTO toDTO(Invoice invoice) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoice.getId());
        invoiceDTO.setCustomerId(invoice.getCustomerId());
        invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceDTO.setTotalAmount(invoice.getTotalAmount());
        return invoiceDTO;
    }

    public Invoice toEntity(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDTO.getId());
        invoice.setCustomerId(invoiceDTO.getCustomerId());
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        return invoice;
    }

    public List<InvoiceDTO> toDTOList(List<Invoice> invoices) {
        return invoices.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Invoice> toEntityList(List<InvoiceDTO> invoiceDTOs) {
        return invoiceDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }


}