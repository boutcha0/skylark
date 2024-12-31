package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.DTO.OrderDTO;
import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.DTO.PaymentRequest;
import com.crudops.skylark.service.InfosService;
import com.crudops.skylark.service.OrderService;
import com.crudops.skylark.service.impl.PaymentService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentRestController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final InfosService infosService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            String clientSecret = paymentService.createPaymentIntent(request);
            return ResponseEntity.ok(clientSecret);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate-invoice/{orderId}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrderById(orderId);
            InfosDTO customerInfo = infosService.getInfosById(order.getInfoId());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("SKYLARK")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold());

            document.add(new Paragraph("Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());

            String invoiceNumber = "INV-" + orderId + "-" +
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            document.add(new Paragraph("Invoice Number: " + invoiceNumber)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Invoice Date: " +
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Customer Details")
                    .setFontSize(14)
                    .setBold());
            document.add(new Paragraph("Customer ID: " + customerInfo.getId()));
            document.add(new Paragraph("Name: " + customerInfo.getName()));
            document.add(new Paragraph("Email: " + customerInfo.getEmail()));

            // Shipping Address
            if (order.getShippingAddress() != null) {
                document.add(new Paragraph("Shipping Address:")
                        .setFontSize(12)
                        .setBold());
                document.add(new Paragraph(String.format("%s\n%s, %s\n%s, %s",
                        order.getShippingAddress().getStreetAddress(),
                        order.getShippingAddress().getCity(),
                        order.getShippingAddress().getState(),
                        order.getShippingAddress().getPostalCode(),
                        order.getShippingAddress().getCountry())));
            }

            Table table = new Table(UnitValue.createPercentArray(new float[]{40, 15, 15, 30}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setMarginTop(20);

            table.addHeaderCell("Item Description");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Price");
            table.addHeaderCell("Total");

            for (OrderItemDTO item : order.getOrderItems()) {
                table.addCell(item.getProductId().toString()); // Item Description
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("$" + String.format("%.2f", item.getUnitPrice()));
                table.addCell("$" + String.format("%.2f", item.getTotalAmount()));
            }

            document.add(table);

            document.add(new Paragraph("").setMarginTop(10));

            document.add(new Paragraph("Total Amount: $" + String.format("%.2f", order.getTotalAmount()))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(16)
                    .setBold());

            document.add(new Paragraph("Order Status: " + order.getStatus())
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20));

            document.add(new Paragraph("Thank you for shopping with SKYLARK!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50));

            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + invoiceNumber + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(("Error generating invoice: " + e.getMessage()).getBytes());
        }
    }
}