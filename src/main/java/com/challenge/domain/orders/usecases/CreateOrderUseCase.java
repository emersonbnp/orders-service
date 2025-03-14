package com.challenge.domain.orders.usecases;

import com.challenge.domain.orders.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;
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

    public String execute(Order order) throws DuplicateOrderException {
        logger.debug("Creating order: {}", order);
        var deduplicationCode = order.orderDeduplicationCode();
        if (cacheService.exists(deduplicationCode)) {
            logger.warn("Order already exists.");
            throw new DuplicateOrderException(String.format("Duplicate order for customer %s", order.getCustomerUuid()));
        }
        cacheService.set(deduplicationCode);

        try {
            var totalPrice = order.getItems()
                    .stream()
                    .map(o -> o.getPrice() * o.getQuantity())
                    .reduce(Double::sum);
            order.setTotalPrice(totalPrice.orElse(0.0));

            var orderUuid = orderRepository.saveOrder(order);
            logger.info("Created order uuid: {}", orderUuid);
            return orderUuid;
        } catch (Exception e) {
            logger.error("Error creating order", e);
            cacheService.remove(deduplicationCode);
            throw e;
        }
    }
}
