package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.PaymentDTO;
import com.crudops.skylark.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentsRestController {

    private final PaymentsService paymentsService;

    @Autowired
    public PaymentsRestController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    // Create a new payment
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentsService.createPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    // Retrieve a payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO payment = paymentsService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    // Retrieve all payments
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentsService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Update an existing payment
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentsService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    // Delete a payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentsService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
