package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/download/{invoiceId}")
    public ResponseEntity<?> downloadInvoicePdf(@PathVariable Long invoiceId) {
        try {
            byte[] pdfData = invoiceService.generateInvoicePdf(invoiceId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=invoice-" + invoiceId + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating PDF: " + e.getMessage());
        }
    }

}