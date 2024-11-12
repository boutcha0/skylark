package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.PaymentDTO;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDTO toDto(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId()); // Retrieve `Order` ID
        dto.setPaymentAmount(payment.getPaymentAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        return dto;
    }

    public Payment toEntity(PaymentDTO dto, Order order) {
        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setOrder(order); // Set `Order` entity in `Payment`
        payment.setPaymentAmount(dto.getPaymentAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(dto.getStatus());
        return payment;
    }
}
