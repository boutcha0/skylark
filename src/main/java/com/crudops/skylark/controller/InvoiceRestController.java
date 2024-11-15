package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.service.impl.InvoiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceRestController {

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @PostMapping("/generate")
    public ResponseEntity<InvoiceDTO> generateInvoice(
            @RequestParam Long customerId, @RequestParam String invoiceDate) {

        try {
            List<Order> orders = invoiceService.getOrdersByCustomerId(customerId);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new InvoiceDTO("No orders found for this customer"));
            }

            // Logic to generate the invoice
            InvoiceDTO invoice = generateInvoiceForOrders(orders, invoiceDate);

            return ResponseEntity.ok(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InvoiceDTO("An unexpected error occurred: " + e.getMessage()));
        }
    }

    private InvoiceDTO generateInvoiceForOrders(List<Order> orders, String invoiceDate) {
        // Example logic to process the orders and generate the invoice
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        double totalAmount = 0;

        for (Order order : orders) {
            totalAmount += order.getAmount(); // Summing the order amounts
        }

        invoiceDTO.setTotalAmount(totalAmount);
        invoiceDTO.setInvoiceDate(LocalDate.parse(invoiceDate));
        return invoiceDTO;
    }
}
