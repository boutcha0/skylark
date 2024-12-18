package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.model.OrderItem;
import com.crudops.skylark.model.Product;
import com.crudops.skylark.repository.OrderItemRepository;
import com.crudops.skylark.repository.ProductRepository;
import com.crudops.skylark.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDTO> getOrderItems() {
        return orderItemRepository.findAll()
                .stream()
                .map(orderItem -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setId(orderItem.getId());
                    dto.setProductId(orderItem.getProduct().getId());
                    dto.setQuantity(orderItem.getQuantity());
                    dto.setTotalPrice(orderItem.getTotalPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDTO getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .map(orderItem -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setId(orderItem.getId());
                    dto.setProductId(orderItem.getProduct().getId());
                    dto.setQuantity(orderItem.getQuantity());
                    dto.setTotalPrice(orderItem.getTotalPrice());
                    return dto;
                })
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + id));
    }

    @Override
    @Transactional
    public OrderItemDTO createOrderItem(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // Return the DTO
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(savedOrderItem.getId());
        dto.setProductId(savedOrderItem.getProduct().getId());
        dto.setQuantity(savedOrderItem.getQuantity());
        dto.setTotalPrice(savedOrderItem.getTotalPrice());
        return dto;
    }

    @Override
    @Transactional
    public OrderItemDTO updateOrderItem(Long id, Integer quantity) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + id));

        existingOrderItem.setQuantity(quantity);
        OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);

        // Return the DTO
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(updatedOrderItem.getId());
        dto.setProductId(updatedOrderItem.getProduct().getId());
        dto.setQuantity(updatedOrderItem.getQuantity());
        dto.setTotalPrice(updatedOrderItem.getTotalPrice());
        return dto;
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + id));

        orderItemRepository.delete(orderItem);
    }

    @Override
    public Double calculateTotalPrice() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }
}
