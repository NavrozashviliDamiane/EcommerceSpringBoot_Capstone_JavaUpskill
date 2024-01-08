package com.damiane.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private String transactionId;

}
