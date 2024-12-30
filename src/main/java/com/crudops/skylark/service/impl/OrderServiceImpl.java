package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.OrderDTO;
import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.model.*;
import com.crudops.skylark.repository.*;
import com.crudops.skylark.mapper.OrderMapper;
import com.crudops.skylark.service.OrderService;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InfosRepository infoRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            InfosRepository infoRepository,
                            ProductRepository productRepository,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.infoRepository = infoRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        Info info = infoRepository.findById(String.valueOf(orderDTO.getInfoId()))
                .orElseThrow(() -> new RuntimeException("Info not found"));

        order.setInfo(info);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalAmount(product.getPrice() * itemDTO.getQuantity());
            orderItem.setTotalPrice(product.getPrice() * itemDTO.getQuantity());

            orderItems.add(orderItem);
            totalAmount += orderItem.getTotalAmount();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        if (orderDTO.getShippingAddress() != null) {
            ShippingAddress address = new ShippingAddress();
            address.setStreetAddress(orderDTO.getShippingAddress().getStreetAddress());
            address.setCity(orderDTO.getShippingAddress().getCity());
            address.setState(orderDTO.getShippingAddress().getState());
            address.setPostalCode(orderDTO.getShippingAddress().getPostalCode());
            address.setCountry(orderDTO.getShippingAddress().getCountry());
            address.setOrder(order);
            order.setShippingAddress(address);
        }


        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByInfoId(Long infoId) {
        List<Order> orders = orderRepository.findByInfoId(infoId);
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(orderDTO.getStatus());

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


    @Transactional
    public void syncOrderToStripe(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName("Order #" + order.getId())
                .setDescription("Order details for user ID: " + order.getInfo().getId())
                .putMetadata("orderId", order.getId().toString())
                .putMetadata("userId", order.getInfo().getId().toString())
                .build();

        try {
            com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

            for (OrderItem orderItem : order.getOrderItems()) {
                PriceCreateParams priceParams = PriceCreateParams.builder()
                        .setUnitAmount((long) (orderItem.getUnitPrice() * 100))
                        .setCurrency("usd")
                        .setProduct(String.valueOf(stripeProduct.getId()))
                        .setNickname(orderItem.getProduct().getName() + " x " + orderItem.getQuantity())
                        .build();

                Price.create(priceParams);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error syncing order to Stripe: " + e.getMessage(), e);
        }
    }

}
