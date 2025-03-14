package com.challenge.domain.orders.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Order {
    private String customerUuid;
    private String sellerUuid;
    private OrderStatusEnum status;
    private List<OrderItem> items;
    private Double totalPrice;

    public Order(String customerUuid, String sellerUuid, OrderStatusEnum status, List<OrderItem> items) {
        this.customerUuid = customerUuid;
        this.sellerUuid = sellerUuid;
        this.status = status;
        this.items = items;
    }

    public Order(String customerUuid, String sellerUuid) {
        this.customerUuid = customerUuid;
        this.sellerUuid = sellerUuid;
    }



    public Order() {
    }

    // interpolates customer uuid with hashcode to ensure no collision
    public String orderDeduplicationCode() {
        return String.format("%s.%s", customerUuid, this.hashCode());
    }
}
