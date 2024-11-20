package com.crudops.skylark.service;

import com.crudops.skylark.DTO.PaymentDTO;

import java.util.List;

public interface PaymentsService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Long id);
    List<PaymentDTO> getAllPayments();
    PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO);
    void deletePayment(Long id);

}
