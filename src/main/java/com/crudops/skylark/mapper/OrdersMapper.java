package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.OrdersDTO;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersMapper {

    public OrdersDTO toDTO(Order order) {
        OrdersDTO orderDTO = new OrdersDTO();
        orderDTO.setId(order.getId());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCustomerId(order.getCustomerId());
        orderDTO.setInvoiceId(order.getInvoiceId());
        return orderDTO;
    }

    public Order toEntity(OrdersDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setAmount(orderDTO.getAmount());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(orderDTO.getStatus());
        order.setCustomerId(orderDTO.getCustomerId());
        order.setInvoiceId(orderDTO.getInvoiceId());
        return order;
    }

    public List<OrdersDTO> toDTOList(List<Order> orders) {
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Order> toEntityList(List<OrdersDTO> orderDTOs) {
        return orderDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }public OrdersDTO toDto(Order order) {
        OrdersDTO dto = new OrdersDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setAmount(order.getAmount());
        return dto;
    }

    public Order toEntity(OrdersDTO dto, Info customer) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setCustomerId(customer.getId());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());
        order.setAmount(dto.getAmount());
        return order;
    }
}