package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.infrastructure.data.entities.OrderEntity;
import com.challenge.infrastructure.data.entities.OrderItemEntity;

import java.util.UUID;

public enum OrderMapper {
    INSTANCE;

    public OrderEntity toOrderEntity(Order order) {
        var orderItemsEntity = order.getItems()
                .stream()
                .map(o ->
                        new OrderItemEntity(
                                o.getProductUuid(),
                                o.getQuantity(),
                                o.getPrice()
                        )
                ).toList();

        return new OrderEntity(
                order.getCustomerUuid(),
                order.getSellerUuid(),
                orderItemsEntity
        );
    }

    public Order fromOrderEntity(OrderEntity orderEntity) {
        var orderItems = orderEntity.getOrderItems()
                .stream()
                .map(o -> new OrderItem(o.getProductUuid().toString(), o.getQuantity(), o.getPrice()))
                .toList();

        return new Order(
                orderEntity.getCustomerUuid(),
                orderEntity.getSellerUuid(),
                orderItems
        );
    }
}
