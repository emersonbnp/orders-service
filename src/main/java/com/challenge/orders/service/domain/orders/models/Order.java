package com.challenge.orders.service.domain.orders.models;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String customerId;
    private String sellerId;
    private OrderStatusEnum status;
    private List<OrderItem> items;
    private Double totalPrice;
}
