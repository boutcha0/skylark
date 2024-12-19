package com.crudops.skylark.service;

import com.crudops.skylark.DTO.OrderDTO;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getOrdersByInfoId(Long infoId);
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
}