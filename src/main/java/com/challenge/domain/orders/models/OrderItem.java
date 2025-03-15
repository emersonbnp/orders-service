package com.challenge.domain.orders.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderItem {
    private String productUuid;
    private Integer quantity;
    private Double price;
}
