package com.challenge.infrastructure.data;

import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.infrastructure.data.entities.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoOrderDAO extends OrderRepository, MongoRepository<OrderEntity, UUID> {
}
