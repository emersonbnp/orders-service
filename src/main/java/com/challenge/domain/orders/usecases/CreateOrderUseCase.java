package com.challenge.domain.orders.usecases;

import com.challenge.domain.orders.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.domain.services.CacheService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateOrderUseCase {

    @NonNull
    private final CacheService cacheService;
    @NonNull
    private final OrderRepository orderRepository;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Transactional
    public void execute(Order order) throws DuplicateOrderException {
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
                    .map(o -> o.getPrice().multiply(BigDecimal.valueOf(o.getQuantity())))
                    .reduce((item, acc) -> acc.add(item));
            order.setTotalPrice(totalPrice.orElse(BigDecimal.valueOf(0.0)));

            var orderUuid = orderRepository.saveOrder(order);
            logger.info("Created order uuid: {}", orderUuid);
        } catch (Exception e) {
            logger.error("Error creating order", e);
            cacheService.remove(deduplicationCode);
            throw e;
        }
    }
}
