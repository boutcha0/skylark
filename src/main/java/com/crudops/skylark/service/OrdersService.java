package com.crudops.skylark.service;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.DTO.OrdersDTO;
import com.crudops.skylark.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrdersService {
    OrdersDTO createOrder(OrdersDTO orderDTO);
    OrdersDTO getOrderById(Long id);
    List<OrdersDTO> getAllOrders();

}