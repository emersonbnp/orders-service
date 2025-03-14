package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoOrderRepository implements OrderRepository {

    private final MongoOrderDAO orderDAO;

    public MongoOrderRepository(final MongoOrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public String saveOrder(Order order) {
        var orderEntity = OrderMapper.INSTANCE.toOrderEntity(order);

        return orderDAO.save(orderEntity)
                .getOrderUuid();
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        var orderEntity = orderDAO.findById(UUID.fromString(orderId));
        return orderEntity.map(OrderMapper.INSTANCE::fromOrderEntity);
    }
}
