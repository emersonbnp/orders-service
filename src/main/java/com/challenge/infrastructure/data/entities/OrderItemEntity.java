package com.challenge.infrastructure.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemEntity {
    private String productUuid;
    private Integer quantity;
    private Double price;
}
