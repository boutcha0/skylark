package com.crudops.skylark.service;

import com.crudops.skylark.DTO.OrdersDTO;
import java.util.List;

public interface OrdersService {
    OrdersDTO createOrder(OrdersDTO orderDTO);
    OrdersDTO getOrderById(Long id);
    List<OrdersDTO> getAllOrders();
    OrdersDTO updateOrder(Long id, OrdersDTO orderDTO);
    void deleteOrder(Long id);
}
