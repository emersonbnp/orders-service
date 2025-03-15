package com.challenge.infrastructure.data;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderFilter;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.domain.orders.repository.Paging;
import com.challenge.infrastructure.data.entities.OrderEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    public List<Order> getOrderByFilter(OrderFilter orderFilter, Paging paging) {
        Criteria criteria = new Criteria();

        List<Criteria> filters = new ArrayList<>();
        if (StringUtils.isNotBlank(orderFilter.customer())) {
            filters.add(Criteria.where("customerUuid").is(orderFilter.customer()));
        }
        if (StringUtils.isNotBlank(orderFilter.seller())) {
            filters.add(Criteria.where("sellerUuid").is(orderFilter.seller()));
        }

        if (!filters.isEmpty()) {
            criteria.andOperator(filters.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria);
        query.skip((long) paging.page() * paging.size());
        query.limit(paging.size());

        var result = mongoTemplate.find(query, OrderEntity.class);
        return result.stream()
                .map(OrderMapper.INSTANCE::fromOrderEntity)
                .toList();
    }
}
