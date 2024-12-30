package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.OrderDTO;
import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.DTO.ShippingAddressDTO;
import com.crudops.skylark.model.Order;
import com.crudops.skylark.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setInfoId(order.getInfo().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(orderItemDTOs);

        if (order.getShippingAddress() != null) {
            ShippingAddressDTO addressDTO = new ShippingAddressDTO();
            addressDTO.setId(order.getShippingAddress().getId());
            addressDTO.setStreetAddress(order.getShippingAddress().getStreetAddress());
            addressDTO.setCity(order.getShippingAddress().getCity());
            addressDTO.setState(order.getShippingAddress().getState());
            addressDTO.setPostalCode(order.getShippingAddress().getPostalCode());
            addressDTO.setCountry(order.getShippingAddress().getCountry());
            dto.setShippingAddress(addressDTO);
        }

        return dto;
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setTotalAmount(orderItem.getTotalAmount());
        return dto;
    }
}
