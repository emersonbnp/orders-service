package com.challenge.orders.service.domain.orders.models;

import lombok.Data;

@Data
public class OrderItem {
    private String productId;
    private Integer quantity;
    private Double price;
}
