package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.infrastructure.data.entities.OrderEntity;
import com.challenge.infrastructure.data.entities.OrderItemEntity;

import java.util.List;
import java.util.UUID;

public class OrderFixture {
    public static final UUID ORDER_UUID = UUID.randomUUID();
    public static final UUID CUSTOMER_UUID = UUID.randomUUID();
    public static final UUID SELLER_UUID = UUID.randomUUID();
    public static final UUID PRODUCT_UUID = UUID.randomUUID();
    public static final Integer QUANTITY = 1;
    public static final Double PRICE = 1.0;

    private OrderFixture() {
    }

    public static OrderItem instaceOfOrderItem() {
        return instaceOfOrderItem(null, null, null);
    }

    public static OrderItem instaceOfOrderItem(
            String productUuid,
            Integer quantity,
            Double price
    ) {
        return new OrderItem(
                productUuid != null ? productUuid : PRODUCT_UUID.toString(),
                quantity != null ? quantity : QUANTITY,
                price != null ? price : PRICE
        );
    }

    public static Order instanceOfOrder() {
        return instanceOfOrder(null, null, null);
    }

    public static Order instanceOfOrder(
            UUID customerUuid,
            UUID sellerUuid,
            List<OrderItem> orderItems
    ) {
        return new Order(
                customerUuid != null ? customerUuid.toString() : CUSTOMER_UUID.toString(),
                sellerUuid != null ? sellerUuid.toString() : SELLER_UUID.toString(),
                orderItems != null ? orderItems : List.of(instaceOfOrderItem(null, null, null))
        );
    }

    public static OrderItemEntity instaceOfOrderItemEntity() {
        return instaceOfOrderItemEntity(null, null, null);
    }

    public static OrderItemEntity instaceOfOrderItemEntity(
            UUID productUuid,
            Integer quantity,
            Double price
    ) {
        return new OrderItemEntity(
                productUuid != null ? productUuid : PRODUCT_UUID,
                quantity != null ? quantity : QUANTITY,
                price != null ? price : PRICE
        );
    }

    public static OrderEntity instanceOfOrderEntity() {
        return instanceOfOrderEntity(null, null, null, null);
    }

    public static OrderEntity instanceOfOrderEntity(
            UUID orderUuid,
            UUID customerUuid,
            UUID sellerUuid,
            List<OrderItemEntity> orderItems
    ) {
        return new OrderEntity(
                orderUuid != null ? orderUuid : ORDER_UUID,
                customerUuid != null ? customerUuid : CUSTOMER_UUID,
                sellerUuid != null ? sellerUuid : SELLER_UUID,
                orderItems != null ? orderItems : List.of(instaceOfOrderItemEntity(null, null, null))
        );
    }
}
