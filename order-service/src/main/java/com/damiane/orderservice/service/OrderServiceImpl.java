package com.damiane.orderservice.service;

import com.damiane.orderservice.entity.Order;
import com.damiane.orderservice.entity.OrderStatus;
import com.damiane.orderservice.external.client.AccountService;
import com.damiane.orderservice.external.client.ProductService;
import com.damiane.orderservice.model.OrderRequest;
import com.damiane.orderservice.respository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;



    @Override
    public long createOrder(OrderRequest orderRequest) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        Order order = Order.builder()
                .totalAmount(orderRequest.getTotalAmount())
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .shippingAddress(orderRequest.getShippingAddress())
                .paymentMethod(orderRequest.getPaymentMethod())
                .transactionId(orderRequest.getTransactionId())
                .createdAt(currentDateTime)
                .updatedAt(currentDateTime)
                .build();



        order = orderRepository.save(order);


        accountService.addOrderIdsToAccount(orderRequest.getUserId(), order.getOrderId());

        return order.getOrderId();

    }


    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        optionalOrder.ifPresent(order -> {
            order.setOrderStatus(newStatus);
            orderRepository.save(order);
        });
    }

    @Override
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

}
