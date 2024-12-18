package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.model.OrderItem;
import com.crudops.skylark.model.Product;
import com.crudops.skylark.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductRepository productRepository;

    // Convert OrderItem to DTO
    public OrderItemDTO toDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setTotalPrice(orderItem.getTotalPrice());
        return dto;
    }

    // Convert DTO to OrderItem
    public OrderItem toEntity(OrderItemDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setProduct(product);

        // Set quantity, use 1 as default if not provided
        Integer quantity = dto.getQuantity() != null ? dto.getQuantity() : 1;
        orderItem.setQuantity(quantity);

        // Use product price if unit price is not provided
        Double unitPrice = dto.getUnitPrice() != null ? dto.getUnitPrice() : product.getPrice();
        orderItem.setUnitPrice(unitPrice);

        // Calculate total price
        orderItem.setTotalPrice(orderItem.calculateTotalPrice());

        return orderItem;
    }
}