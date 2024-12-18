package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.mapper.OrderItemMapper;
import com.crudops.skylark.model.OrderItem;
import com.crudops.skylark.repository.OrderItemRepository;
import com.crudops.skylark.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderItemDTO addOrderItem(OrderItemDTO orderItemDTO) {
        // Check if the item already exists in the repository based on productId
        Optional<OrderItem> existingOrderItemOptional = orderItemRepository.findByProductId(orderItemDTO.getProductId());

        OrderItem orderItem;
        if (existingOrderItemOptional.isPresent()) {
            // Update existing order item
            orderItem = existingOrderItemOptional.get();
            orderItem.setQuantity(orderItem.getQuantity() + orderItemDTO.getQuantity());
            orderItem.setTotalPrice(orderItem.getQuantity() * orderItem.getUnitPrice());
        } else {
            // Create a new order item
            orderItem = orderItemMapper.toEntity(orderItemDTO);
            orderItem.setTotalPrice(orderItem.getQuantity() * orderItem.getUnitPrice());
        }

        // Save to the repository
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.toDTO(savedOrderItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll()
            .stream()
            .map(orderItemMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDTO getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
            .map(orderItemMapper::toDTO)
            .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: " + id));
    }


    @Override
    @Transactional
    public void removeOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: " + id));

        orderItemRepository.delete(orderItem);
    }

    @Override
    @Transactional
    public void deleteOrderItemByProductId(Long productId) {
        // Delete all order items associated with the productId
        orderItemRepository.deleteByProductId(productId);
    }
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalCartValue() {
        return orderItemRepository.findAll()
            .stream()
            .mapToDouble(OrderItem::getTotalPrice)
            .sum();
    }
}