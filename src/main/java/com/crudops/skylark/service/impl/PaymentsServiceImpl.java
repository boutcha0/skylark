package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.PaymentDTO;
import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.mapper.PaymentMapper;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.model.Payment;
import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.repository.PaymentsRepository;
import com.crudops.skylark.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentsRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentsServiceImpl(PaymentsRepository paymentRepository, OrdersRepository ordersRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        // Fetch the associated Order
        Order order = ordersRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new InfosNotFoundException("Order with ID " + paymentDTO.getOrderId() + " not found"));

        // Map DTO to entity and set the Order
        Payment payment = paymentMapper.toEntity(paymentDTO, order);
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Payment with ID " + id + " not found"));
        return paymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Payment with ID " + id + " not found"));

        // Fetch the associated Order for updating
        Order order = ordersRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new InfosNotFoundException("Order with ID " + paymentDTO.getOrderId() + " not found"));

        // Update the fields of the existing payment
        existingPayment.setOrder(order);
        existingPayment.setPaymentAmount(paymentDTO.getPaymentAmount());
        existingPayment.setPaymentDate(paymentDTO.getPaymentDate());
        existingPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        existingPayment.setStatus(paymentDTO.getStatus());

        Payment updatedPayment = paymentRepository.save(existingPayment);
        return paymentMapper.toDto(updatedPayment);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Payment with ID " + id + " not found"));
        paymentRepository.delete(payment);
    }
}
