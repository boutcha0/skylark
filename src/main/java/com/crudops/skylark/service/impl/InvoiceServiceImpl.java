package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.repository.InvoiceRepository;
import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.service.InvoiceService;
import org.springframework.stereotype.Service;
import com.crudops.skylark.mapper.InvoiceMapper;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrdersRepository orderRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, OrdersRepository orderRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        // Fetch all orders for the given customerId
        List<Order> byCustomerId = (List<Order>) orderRepository.findByCustomerId(customerId);
        return byCustomerId;
    }

    @Override
    public InvoiceDTO generateInvoice(Long customerId, LocalDate invoiceDate) {
        // Find all orders for the customer on the specified invoiceDate
        List<Order> orders = orderRepository.findByCustomerIdAndOrderDate(customerId, invoiceDate);

        // Ensure we have orders for the specified date
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders found for the specified customer and date.");
        }

        // Calculate the total amount for the invoice by summing the order amounts
        double totalAmount = orders.stream()
                .mapToDouble(Order::getAmount)
                .sum();

        // Create the invoice entity and set its details
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setInvoiceDate(invoiceDate);
        invoice.setTotalAmount(totalAmount);

        // Save the invoice entity to the database
        invoice = invoiceRepository.save(invoice);

        // Map the entity to DTO and return
        return invoiceMapper.toDto(invoice);
    }
}
