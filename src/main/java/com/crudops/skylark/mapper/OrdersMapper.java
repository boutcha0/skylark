package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.OrdersDTO;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrdersMapper {

    public OrdersDTO toDto(Order order) {
        OrdersDTO dto = new OrdersDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setAmount(order.getAmount());
        return dto;
    }

    public Order toEntity(OrdersDTO dto, Info customer) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setCustomer(customer);
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());
        order.setAmount(dto.getAmount());
        return order;
    }
}