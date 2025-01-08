package com.crudops.skylark.service;

import com.crudops.skylark.DTO.OrderDTO;
import com.crudops.skylark.DTO.ShippingAddressDTO;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.model.ShippingAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getOrdersByInfoId(Long infoId);
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);

    void syncOrderToStripe(Order order);

    OrderDTO calculateOrder(OrderDTO orderDTO);

    ShippingAddress createShippingAddress(ShippingAddressDTO addressDTO, Order order);


    @Transactional(readOnly = true)
    List<OrderDTO> getFilteredOrders(Long infoId, String orderId, LocalDateTime startDate, LocalDateTime endDate);
}