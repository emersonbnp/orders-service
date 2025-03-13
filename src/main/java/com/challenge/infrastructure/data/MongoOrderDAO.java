package com.challenge.infrastructure.data;

import com.challenge.infrastructure.data.entities.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoOrderDAO extends MongoRepository<OrderEntity, UUID> {
}
