package com.challenge.domain.orders.domain.orders.usecases;

import com.challenge.domain.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.domain.orders.models.Order;
import com.challenge.domain.orders.domain.orders.repository.OrderRepository;
import com.challenge.domain.services.CacheService;

public class CreateOrderUseCase {

    private final CacheService cacheService;
    private final OrderRepository orderRepository;

    public CreateOrderUseCase(CacheService cacheService, OrderRepository orderRepository) {
        this.cacheService = cacheService;
        this.orderRepository = orderRepository;
    }

    public String execute(Order order) {
        if (cacheService.exists(order.orderDeduplicationCode())) {
            throw new DuplicateOrderException(String.format("Duplicate order for customer %s", order.getCustomerUuid()));
        }

        var totalPrice = order.getItems()
                .stream()
                .map(o -> o.getPrice() * o.getQuantity())
                .reduce(Double::sum);
        order.setTotalPrice(totalPrice.orElse(0.0));

        return orderRepository.saveOrder(order);
    }
}
