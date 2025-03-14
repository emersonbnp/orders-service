package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.domain.orders.models.OrderStatusEnum;
import com.challenge.infrastructure.data.entities.OrderEntity;
import com.challenge.infrastructure.data.entities.OrderItemEntity;

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
                order.getStatus(),
                orderItemsEntity,
                order.getTotalPrice()
        );
    }

    public Order fromOrderEntity(OrderEntity orderEntity) {
        var orderItems = orderEntity.getOrderItems()
                .stream()
                .map(o -> new OrderItem(o.getProductUuid(), o.getQuantity(), o.getPrice()))
                .toList();

        return new Order(
                orderEntity.getCustomerUuid(),
                orderEntity.getSellerUuid(),
                OrderStatusEnum.valueOf(orderEntity.getStatus()),
                orderItems,
                orderEntity.getTotalPrice()
        );
    }
}
