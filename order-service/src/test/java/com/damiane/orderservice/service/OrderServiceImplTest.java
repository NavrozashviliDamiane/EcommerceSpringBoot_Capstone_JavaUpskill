package com.damiane.orderservice.service;

import com.damiane.orderservice.entity.Order;
import com.damiane.orderservice.entity.OrderStatus;
import com.damiane.orderservice.external.client.AccountService;
import com.damiane.orderservice.external.client.ProductService;
import com.damiane.orderservice.model.OrderRequest;
import com.damiane.orderservice.respository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setQuantity(1);
        Long userId = 1L;

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(123L);
            return order;
        });

        Long orderId = orderService.createOrder(orderRequest);

        verify(productService, times(1)).reduceQuantity(anyLong(), anyInt());
        verify(orderRepository, times(1)).save(any(Order.class));

        assertNotNull(orderId);

    }

    @Test
    void getOrdersByUserId() {
        Long userId = 1L;
        when(orderRepository.findByUserId(userId)).thenReturn(Collections.singletonList(new Order()));

        List<Order> orders = orderService.getOrdersByUserId(userId);

        assertEquals(1, orders.size());
    }

    @Test
    void updateOrderStatus() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.CREATED;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, newStatus);

        assertEquals(newStatus, order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void cancelOrder() {
        Long orderId = 1L;

        orderService.cancelOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void getOrderById() {
        Long orderId = 1L;
        Order expectedOrder = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Order order = orderService.getOrderById(orderId);

        assertEquals(expectedOrder, order);
    }
}
