package com.damiane.orderservice.controller;

import com.damiane.orderservice.entity.Order;
import com.damiane.orderservice.entity.OrderStatus;
import com.damiane.orderservice.model.OrderRequest;
import com.damiane.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrderTest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(1L);
        orderRequest.setProductId(10L);
        orderRequest.setQuantity(3);
        orderRequest.setTotalAmount(150);
        orderRequest.setShippingAddress("123 Avenue St, City");
        orderRequest.setPaymentMethod("Credit Card");
        orderRequest.setTransactionId("TXN9876");

        Long orderId = 101L;
        when(orderService.createOrder(orderRequest)).thenReturn(orderId);

        ResponseEntity<Long> response = orderController.placeOrder(orderRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderId, response.getBody());
    }

    @Test
    void getOrdersByUserIdTest() {
        Long userId = 1L;
        Order sampleOrder = Order.builder()
                .orderId(201L)
                .userId(userId)
                .productId(15L)
                .quantity(2)
                .totalAmount(90)
                .orderStatus(OrderStatus.PENDING)
                .shippingAddress("456 Street Ave, Town")
                .paymentMethod("PayPal")
                .transactionId("TXN555")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Order> orders = Collections.singletonList(sampleOrder);
        when(orderService.getOrdersByUserId(userId)).thenReturn(orders);

        List<Order> result = orderController.getOrdersByUserId(userId);

        assertEquals(orders, result);
    }

    @Test
    void updateOrderStatusTest() {
        Long orderId = 301L;
        OrderStatus newStatus = OrderStatus.SHIPPED;

        doNothing().when(orderService).updateOrderStatus(orderId, newStatus);

        orderController.updateOrderStatus(orderId, newStatus);

        verify(orderService, times(1)).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void cancelOrderTest() {
        Long orderId = 401L;

        doNothing().when(orderService).cancelOrder(orderId);

        orderController.cancelOrder(orderId);

        verify(orderService, times(1)).cancelOrder(orderId);
    }

    @Test
    void getOrderByIdTest() {
        Long orderId = 501L;
        Order sampleOrder = Order.builder()
                .orderId(orderId)
                .userId(5L)
                .productId(25L)
                .quantity(1)
                .totalAmount(30)
                .orderStatus(OrderStatus.PROCESSING)
                .shippingAddress("789 Road Blvd, Village")
                .paymentMethod("Debit Card")
                .transactionId("TXN888")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(orderService.getOrderById(orderId)).thenReturn(sampleOrder);

        Order result = orderController.getOrderById(orderId);

        assertEquals(sampleOrder, result);
    }
}
