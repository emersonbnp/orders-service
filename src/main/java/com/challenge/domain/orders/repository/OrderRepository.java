package com.challenge.domain.orders.repository;

import com.challenge.domain.orders.models.Order;

public interface OrderRepository {
    String saveOrder(Order order);

    Order getOrderById(String orderId);
}
