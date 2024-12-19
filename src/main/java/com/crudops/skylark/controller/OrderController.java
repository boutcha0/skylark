package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.OrderDTO;
import com.crudops.skylark.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/info/{infoId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByInfo(@PathVariable Long infoId) {
        List<OrderDTO> orders = orderService.getOrdersByInfoId(infoId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}