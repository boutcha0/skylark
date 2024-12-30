package com.crudops.skylark.DTO;

import com.crudops.skylark.DTO.OrderItemDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long infoId;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private ShippingAddressDTO shippingAddress;

}
