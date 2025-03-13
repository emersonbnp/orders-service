package com.challenge.infrastructure.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderEntity {

    @Id
    private UUID orderUuid;

    private UUID customerUuid;

    private UUID sellerUuid;

    @Field(name = "items")
    private List<OrderItemEntity> orderItems;

    public OrderEntity(UUID customerUuid, UUID sellerUuid, List<OrderItemEntity> orderItems) {
        this.customerUuid = customerUuid;
        this.sellerUuid = sellerUuid;
        this.orderItems = orderItems;
    }
}
