package com.challenge.entrypoint.kafka.events;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.domain.orders.models.OrderStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderEvent(
        @NotBlank()
        String customerUuid,

        @NotBlank()
        String sellerUuid,

        @Size(min = 1)
        List<CreateOrderItemEvent> items
) {
    public Order toOrder() {
        return new Order(
                customerUuid,
                sellerUuid,
                OrderStatusEnum.CREATED,
                items.stream()
                        .map(o -> new OrderItem(
                                o.productUuid(),
                                o.quantity(),
                                o.price()
                        )).toList(),
                null
        );
    }
}
