package com.crudops.skylark.service;

import com.crudops.skylark.DTO.OrderItemDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderItemService {
    OrderItemDTO addOrderItem(OrderItemDTO orderItemDTO);
    List<OrderItemDTO> getAllOrderItems();
    OrderItemDTO getOrderItemById(Long id);
    void removeOrderItem(Long id);

    @Transactional
    void deleteOrderItemByProductId(Long productId);

    Double calculateTotalCartValue();
}