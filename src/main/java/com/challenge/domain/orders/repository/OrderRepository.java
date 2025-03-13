package com.challenge.domain.orders.repository;

import com.challenge.domain.orders.models.Order;

import java.util.Optional;

public interface OrderRepository {
    String saveOrder(Order order);

    Optional<Order> getOrderById(String orderId);
}
