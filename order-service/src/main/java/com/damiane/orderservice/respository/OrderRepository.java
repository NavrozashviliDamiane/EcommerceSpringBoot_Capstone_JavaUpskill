package com.damiane.orderservice.respository;

import com.damiane.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // You can add custom query methods here if needed
    List<Order> findByUserId(Long userId);

}

