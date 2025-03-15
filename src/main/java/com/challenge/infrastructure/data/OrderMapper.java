package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.domain.orders.models.OrderStatusEnum;
import com.challenge.infrastructure.data.entities.OrderEntity;
import com.challenge.infrastructure.data.entities.OrderItemEntity;

import java.math.RoundingMode;

public enum OrderMapper {
    INSTANCE;

    public OrderEntity toOrderEntity(Order order) {
        var orderItemsEntity = order.getItems()
                .stream()
                .map(o ->
                        new OrderItemEntity(
                                o.getProductUuid(),
                                o.getQuantity(),
                                o.getPrice().setScale(2, RoundingMode.HALF_UP)
                        )
                ).toList();

        return new OrderEntity(
                null,
                order.getCustomerUuid(),
                order.getSellerUuid(),
                order.getStatus().toString(),
                orderItemsEntity,
                order.getTotalPrice().setScale(2, RoundingMode.HALF_UP)
        );
    }

    public Order fromOrderEntity(OrderEntity orderEntity) {
        var orderItems = orderEntity.getOrderItems()
                .stream()
                .map(o -> new OrderItem(
                        o.getProductUuid(),
                        o.getQuantity(),
                        o.getPrice().setScale(2, RoundingMode.HALF_UP)))
                .toList();

        return new Order(
                orderEntity.getCustomerUuid(),
                orderEntity.getSellerUuid(),
                OrderStatusEnum.valueOf(orderEntity.getStatus()),
                orderItems,
                orderEntity.getTotalPrice().setScale(2, RoundingMode.HALF_UP)
        );
    }
}
