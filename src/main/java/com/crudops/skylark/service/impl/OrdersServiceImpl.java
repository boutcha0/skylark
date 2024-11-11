package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.OrdersDTO;
import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.mapper.OrdersMapper;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.repository.InfosRepository;
import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository orderRepository;
    private final InfosRepository infoRepository;
    private final OrdersMapper orderMapper;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository, InfosRepository infosRepository, OrdersMapper ordersMapper) {
        this.orderRepository = ordersRepository;
        this.infoRepository = infosRepository;
        this.orderMapper = ordersMapper;
    }

    @Override
    public OrdersDTO createOrder(OrdersDTO orderDTO) {
        Info customer = infoRepository.findById(String.valueOf(orderDTO.getCustomerId()))
                .orElseThrow(() -> new InfosNotFoundException("Customer with ID " + orderDTO.getCustomerId() + " not found"));

        Order order = orderMapper.toEntity(orderDTO, customer);

        // Set order ID to match customer ID
        order.setId(customer.getId());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrdersDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Order with ID " + id + " not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrdersDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrdersDTO updateOrder(Long id, OrdersDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Order with ID " + id + " not found"));

        Info customer = infoRepository.findById(String.valueOf(orderDTO.getCustomerId()))
                .orElseThrow(() -> new InfosNotFoundException("Customer with ID " + orderDTO.getCustomerId() + " not found"));

        // Ensure the Order ID matches the Customer ID
        existingOrder.setId(customer.getId());

        existingOrder.setCustomer(customer);
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setAmount(orderDTO.getAmount());

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Order with ID " + id + " not found"));
        orderRepository.delete(order);
    }
}
