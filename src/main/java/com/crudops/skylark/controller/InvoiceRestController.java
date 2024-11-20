package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.service.InvoiceService;
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
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getInvoices();
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/generate/{customerId}")
    public ResponseEntity<String> generateInvoices() {
        invoiceService.generateInvoicesForCompletedOrders();
        return ResponseEntity.ok("Invoices generated successfully!");
    }
}