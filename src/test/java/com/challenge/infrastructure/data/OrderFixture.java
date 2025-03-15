package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.domain.orders.models.OrderStatusEnum;
import com.challenge.infrastructure.data.entities.OrderEntity;
import com.challenge.infrastructure.data.entities.OrderItemEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class OrderFixture {
    public static final String ORDER_UUID = UUID.randomUUID().toString();
    public static final String CUSTOMER_UUID = UUID.randomUUID().toString();
    public static final String SELLER_UUID = UUID.randomUUID().toString();
    public static final String PRODUCT_UUID = UUID.randomUUID().toString();
    public static final Integer QUANTITY = 1;
    public static final BigDecimal PRICE = BigDecimal.valueOf(1.00).setScale(2, RoundingMode.UNNECESSARY);
    public static final OrderStatusEnum STATUS = OrderStatusEnum.CREATED;

    private OrderFixture() {
    }

    public static OrderItem instaceOfOrderItem(
            String productUuid,
            Integer quantity,
            BigDecimal price
    ) {
        return new OrderItem(
                productUuid != null ? productUuid : PRODUCT_UUID,
                quantity != null ? quantity : QUANTITY,
                price != null ? price : PRICE
        );
    }

    public static Order instanceOfOrder() {
        return instanceOfOrder(null, null, null, null);
    }

    public static Order instanceOfOrder(
            String customerUuid,
            String sellerUuid,
            OrderStatusEnum status,
            List<OrderItem> orderItems
    ) {
        return new Order(
                customerUuid != null ? customerUuid : CUSTOMER_UUID,
                sellerUuid != null ? sellerUuid : SELLER_UUID,
                status != null ? status : STATUS,
                orderItems != null ? orderItems : List.of(instaceOfOrderItem(null, null, null))
        );
    }

    public static OrderItemEntity instaceOfOrderItemEntity(
            String productUuid,
            Integer quantity,
            BigDecimal price
    ) {
        return new OrderItemEntity(
                productUuid != null ? productUuid : PRODUCT_UUID,
                quantity != null ? quantity : QUANTITY,
                price != null ? price : PRICE
        );
    }

    public static OrderEntity instanceOfOrderEntity() {
        return instanceOfOrderEntity(null, null, null, null, null);
    }

    public static OrderEntity instanceOfOrderEntity(
            String orderUuid,
            String customerUuid,
            String sellerUuid,
            OrderStatusEnum status,
            List<OrderItemEntity> orderItems
    ) {
        return new OrderEntity(
                orderUuid != null ? orderUuid : ORDER_UUID,
                customerUuid != null ? customerUuid : CUSTOMER_UUID,
                sellerUuid != null ? sellerUuid : SELLER_UUID,
                status != null ? status.name() : STATUS.name(),
                orderItems != null ? orderItems : List.of(instaceOfOrderItemEntity(null, null, null)),
                PRICE
        );
    }
}
