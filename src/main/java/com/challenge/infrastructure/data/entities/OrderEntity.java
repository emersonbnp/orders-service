package com.challenge.infrastructure.data.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document(collection = "orders")
public class OrderEntity {

    @Id
    private UUID orderUuid;

    private UUID customerUuid;

    private UUID sellerUuid;

    @Field(name = "items")
    private List<OrderItemEntity> orderItems;
}
