package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.exception.OrderNotFoundException;
import com.crudops.skylark.mapper.InvoiceMapper;
import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.repository.InvoiceRepository;
import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrdersRepository ordersRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, OrdersRepository ordersRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.ordersRepository = ordersRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Order order = ordersRepository.findById(invoiceDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + invoiceDTO.getOrderId() + " not found"));

        Invoice invoice = invoiceMapper.toEntity(invoiceDTO, order);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(savedInvoice);
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
        return invoiceMapper.toDto(invoice);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));

        Order order = ordersRepository.findById(invoiceDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id " + invoiceDTO.getOrderId()));

        invoice.setOrder(order);
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        invoice.setBillingAddress(invoiceDTO.getBillingAddress());
        invoice.setStatus(invoiceDTO.getStatus());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
        invoiceRepository.delete(invoice);
    }
}
