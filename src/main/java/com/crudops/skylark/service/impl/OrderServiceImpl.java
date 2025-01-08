package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.OrderDTO;
import com.crudops.skylark.DTO.OrderItemDTO;
import com.crudops.skylark.DTO.ShippingAddressDTO;
import com.crudops.skylark.model.*;
import com.crudops.skylark.repository.*;
import com.crudops.skylark.mapper.OrderMapper;
import com.crudops.skylark.service.OrderService;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        order.setStatus("PAID");

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

            orderItems.add(orderItem);
            totalAmount += orderItem.getTotalAmount();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        if (orderDTO.getShippingAddress() != null) {
            order.setShippingAddress(createShippingAddress(orderDTO.getShippingAddress(), order));
        }


        Order savedOrder = orderRepository.save(order);
        syncOrderToStripe(order);
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


    @Override
    @Transactional
    public void syncOrderToStripe(Order order) {
        try {
            ProductCreateParams productParams = ProductCreateParams.builder()
                    .setName("Order " + order.getId())
                    .setDescription("Order details for user ID: " + order.getInfo().getId())
                    .putMetadata("orderId", order.getId().toString())
                    .putMetadata("userId", order.getInfo().getId().toString())
                    .build();

            com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

            for (OrderItem orderItem : order.getOrderItems()) {
                PriceCreateParams priceParams = PriceCreateParams.builder()
                        .setUnitAmount((long) (orderItem.getUnitPrice() * 100))
                        .setCurrency("usd")
                        .setProduct(stripeProduct.getId())
                        .setNickname(orderItem.getProduct().getName() + " x " + orderItem.getQuantity())
                        .build();

                Price.create(priceParams);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error syncing order to Stripe: " + e.getMessage(), e);
        }
    }

    @Override
    public OrderDTO calculateOrder(OrderDTO orderDTO) {
        List<OrderItemDTO> calculatedItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            double unitPrice = product.getPrice();
            double itemTotal = unitPrice * itemDTO.getQuantity();

            OrderItemDTO calculatedItem = new OrderItemDTO();
            calculatedItem.setProductId(itemDTO.getProductId());
            calculatedItem.setQuantity(itemDTO.getQuantity());
            calculatedItem.setUnitPrice(unitPrice);
            calculatedItem.setTotalAmount(itemTotal);

            calculatedItems.add(calculatedItem);
            totalAmount += itemTotal;
        }

        orderDTO.setOrderItems(calculatedItems);
        orderDTO.setTotalAmount(totalAmount);
        return orderDTO;
    }

    @Override
    public ShippingAddress createShippingAddress(ShippingAddressDTO addressDTO, Order order) {
        ShippingAddress address = new ShippingAddress();
        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        address.setOrder(order);
        return address;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getFilteredOrders(Long infoId, String orderId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Order> orders = orderRepository.findFilteredOrdersWithPagination(infoId, orderId, startDate, endDate, pageable);
        return orders.map(orderMapper::toDTO);
    }



}