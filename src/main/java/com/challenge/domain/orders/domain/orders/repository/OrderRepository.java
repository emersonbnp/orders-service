package com.challenge.domain.orders.domain.orders.repository;

import com.challenge.domain.orders.domain.orders.models.Order;

public interface OrderRepository {
    String saveOrder(Order order);

    Order getOrderById(String orderId);
}
