package com.challenge.domain.orders.domain.orders.models;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String customerUuid;
    private String sellerUuid;
    private OrderStatusEnum status;
    private List<OrderItem> items;
    private Double totalPrice;

    public Order(String customerUuid, String sellerUuid, List<OrderItem> items) {
        this.customerUuid = customerUuid;
        this.sellerUuid = sellerUuid;
        this.items = items;
    }

    public Order() {
    }

    // interpolates customer uuid with hashcode to ensure no collision
    public String orderDeduplicationCode() {
        return String.format("%s.%s", customerUuid, this.hashCode());
    }
}
