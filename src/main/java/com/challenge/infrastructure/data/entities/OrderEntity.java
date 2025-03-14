package com.challenge.infrastructure.data.entities;

import com.challenge.domain.orders.models.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderEntity {

    @Id
    private String orderUuid;

    @Indexed
    private String customerUuid;

    @Indexed
    private String sellerUuid;

    private String status;

    @Field(name = "items")
    private List<OrderItemEntity> orderItems;

    private Double totalPrice;

    public OrderEntity(
            String customerUuid,
            String sellerUuid,
            OrderStatusEnum status,
            List<OrderItemEntity> orderItems,
            Double totalPrice) {
        this.customerUuid = customerUuid;
        this.sellerUuid = sellerUuid;
        this.status = status.name();
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }
}
