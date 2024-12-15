package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InvoiceRequest;
import com.crudops.skylark.DTO.PaymentRequest;
import com.crudops.skylark.service.impl.PaymentService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import com.stripe.exception.StripeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {
    private final PaymentService paymentService;

    public PaymentRestController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            String clientSecret = paymentService.createPaymentIntent(request);
            return ResponseEntity.ok(clientSecret);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate-invoice")
    public  ResponseEntity<byte[]> generateInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            // Create a byte array output stream to hold the PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Create PDF writer and document
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Invoice Header
            document.add(new Paragraph("Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());

            // Invoice Details
            document.add(new Paragraph("Invoice Date: " +
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Customer ID: " + invoiceRequest.getCustomerId())
                    .setTextAlignment(TextAlignment.LEFT));

            // Create a table for line items
            Table table = new Table(UnitValue.createPercentArray(new float[]{70, 30}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add table headers
            table.addHeaderCell("Description");
            table.addHeaderCell("Amount");

            // Add line item
            table.addCell("Total Purchase");
            table.addCell("$" + invoiceRequest.getTotalPrice());

            document.add(table);

            // Add total
            document.add(new Paragraph("Total Amount: $" + invoiceRequest.getTotalPrice())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(16)
                    .setBold());

            // Close document
            document.close();

            // Prepare response
            byte[] invoiceBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=invoice-" +
                                    invoiceRequest.getCustomerId() +
                                    "-" +
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                                    ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(invoiceBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(("Error generating invoice: " + e.getMessage()).getBytes());
        }
    }
}