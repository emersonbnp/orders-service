package com.challenge.domain.orders.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    @NonNull
    private String customerUuid;
    @NonNull
    private String sellerUuid;
    @NonNull
    private OrderStatusEnum status;
    @NonNull
    private List<OrderItem> items;
    private BigDecimal totalPrice;

    public Order(String customerUuid, String sellerUuid) {
        this.customerUuid = customerUuid;
        this.sellerUuid = sellerUuid;
    }

    // interpolates customer uuid with hashcode to ensure no collision
    public String orderDeduplicationCode() {
        return String.format("%s.%s", customerUuid, this.hashCode());
    }
}
