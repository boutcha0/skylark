package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.exception.ErrorResponse;
import com.crudops.skylark.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItemDTO> addOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO createdOrderItem = orderItemService.addOrderItem(orderItemDTO);
        return new ResponseEntity<>(createdOrderItem, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> orderItems = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        OrderItemDTO orderItem = orderItemService.getOrderItemById(id);
        return ResponseEntity.ok(orderItem);
    }

    @DeleteMapping("/by-product/{productId}")
    public ResponseEntity<Void> deleteOrderItemsByProductId(@PathVariable Long productId) {
        try {
            orderItemService.deleteOrderItemByProductId(productId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeOrderItem(@PathVariable Long id) {
        try {
            orderItemService.removeOrderItem(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Order item not found with id: " + id));
        }
    }
    @GetMapping("/total-cart-value")
    public ResponseEntity<Double> getTotalCartValue() {
        Double totalCartValue = orderItemService.calculateTotalCartValue();
        return ResponseEntity.ok(totalCartValue);
    }
}