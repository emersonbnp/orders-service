package com.challenge.infrastructure.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemEntity {
    private UUID productUuid;
    private Integer quantity;
    private Double price;
}
