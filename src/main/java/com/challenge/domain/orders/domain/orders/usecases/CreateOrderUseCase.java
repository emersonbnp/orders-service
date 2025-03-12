package com.challenge.domain.orders.domain.orders.usecases;

import com.challenge.domain.orders.domain.orders.models.Order;
import com.challenge.domain.orders.domain.orders.repository.OrderRepository;
import com.challenge.domain.services.CacheService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CreateOrderUseCase {

    private final CacheService cacheService;
    private final OrderRepository orderRepository;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public CreateOrderUseCase(CacheService cacheService, OrderRepository orderRepository) {
        this.cacheService = cacheService;
        this.orderRepository = orderRepository;
    }

    public String execute(Order order) {
        return null;
    }
}
