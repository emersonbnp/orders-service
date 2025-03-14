package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.infrastructure.data.entities.OrderEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoOrderRepository implements OrderRepository {

    private final MongoOrderDAO orderDAO;
    private final MongoTemplate mongoTemplate;

    public MongoOrderRepository(final MongoOrderDAO orderDAO, final MongoTemplate mongoTemplate) {
        this.orderDAO = orderDAO;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String saveOrder(Order order) {
        var orderEntity = OrderMapper.INSTANCE.toOrderEntity(order);

        return orderDAO.save(orderEntity).getOrderUuid();
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        var orderEntity = orderDAO.findById(UUID.fromString(orderId));
        return orderEntity.map(OrderMapper.INSTANCE::fromOrderEntity);
    }

    @Override
    public List<Order> getOrderByExample(Order order) {
        Criteria criteria = new Criteria();

        List<Criteria> filters = new ArrayList<>();
        if (order.getCustomerUuid() != null && !order.getCustomerUuid().isBlank()) {
            filters.add(Criteria.where("customerUuid").is(order.getCustomerUuid()));
        }
        if (order.getSellerUuid() != null && !order.getSellerUuid().isBlank()) {
            filters.add(Criteria.where("sellerUuid").is(order.getSellerUuid()));
        }

        if (!filters.isEmpty()) {
            criteria.andOperator(filters.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria);
        var result = mongoTemplate.find(query, OrderEntity.class);
        return result.stream()
                .map(OrderMapper.INSTANCE::fromOrderEntity)
                .toList();
    }
}
