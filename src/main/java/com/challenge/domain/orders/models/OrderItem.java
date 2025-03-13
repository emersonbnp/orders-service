package com.challenge.domain.orders.models;

import lombok.Data;

@Data
public class OrderItem {
    private String productUuid;
    private Integer quantity;
    private Double price;

    public OrderItem(String productUuid, Integer quantity, Double price) {
        this.productUuid = productUuid;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {
    }
}
