package com.challenge.domain.orders.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class OrderItem {
    private String productUuid;
    private Integer quantity;
    private BigDecimal price;
}
