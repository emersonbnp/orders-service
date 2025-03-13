package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;

public class MongoOrderRepository implements OrderRepository {

    private final MongoOrderDAO orderDAO;

    public MongoOrderRepository(final MongoOrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public String saveOrder(Order order) {
        return "";
    }

    @Override
    public Order getOrderById(String orderId) {
        return null;
    }
}
