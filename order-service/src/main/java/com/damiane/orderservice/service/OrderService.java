package com.damiane.orderservice.service;

import com.damiane.orderservice.entity.Order;
import com.damiane.orderservice.entity.OrderStatus;
import com.damiane.orderservice.model.OrderRequest;

import java.util.List;

public interface OrderService {
    long createOrder(OrderRequest orderRequest);

    List<Order> getOrdersByUserId(Long userId);

    void updateOrderStatus(Long orderId, OrderStatus newStatus);

    void cancelOrder(Long orderId);

    Order getOrderById(Long orderId);

}

