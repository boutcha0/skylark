package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.DTO.OrdersDTO;
import com.crudops.skylark.exception.OrderNotFoundException;
import com.crudops.skylark.mapper.OrdersMapper;
import com.crudops.skylark.model.Order;

import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {



    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrdersMapper orderMapper;

    public OrdersServiceImpl(OrdersRepository ordersRepository, OrdersMapper orderMapper) {
        this.ordersRepository = ordersRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrdersDTO> getAllOrders() {
        return orderMapper.toDTOList(ordersRepository.findAll());
    }

    public OrdersDTO getOrderById(Long id) {
        return ordersRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public OrdersDTO createOrder(OrdersDTO orderDTO) {
        Order savedOrder = ordersRepository.save(orderMapper.toEntity(orderDTO));
        return orderMapper.toDTO(savedOrder);
    }
}