package com.challenge.infrastructure.data.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "orders")
public class OrderEntity {

    @Id
    private String orderUuid;

    @Indexed
    @NonNull
    private String customerUuid;

    @Indexed
    @NonNull
    private String sellerUuid;

    @NonNull
    private String status;

    @Field(name = "items")
    @NonNull
    private List<OrderItemEntity> orderItems;

    private Double totalPrice;
}
