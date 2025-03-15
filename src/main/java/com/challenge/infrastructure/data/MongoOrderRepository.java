package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.infrastructure.data.entities.OrderEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MongoOrderRepository implements OrderRepository {

    @NonNull
    private final MongoOrderDAO orderDAO;
    @NonNull
    private final MongoTemplate mongoTemplate;

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
    public List<Order> getOrderByFilter(String customer, String seller) {
        Criteria criteria = new Criteria();

        List<Criteria> filters = new ArrayList<>();
        if (customer != null && customer.isBlank()) {
            filters.add(Criteria.where("customerUuid").is(customer));
        }
        if (seller != null && !seller.isBlank()) {
            filters.add(Criteria.where("sellerUuid").is(seller));
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
