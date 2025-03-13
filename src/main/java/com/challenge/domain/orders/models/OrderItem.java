package com.challenge.domain.orders.models;

import lombok.Data;

@Data
public class OrderItem {
    private String productId;
    private Integer quantity;
    private Double price;

    public OrderItem(String productId, Integer quantity, Double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {
    }
}
