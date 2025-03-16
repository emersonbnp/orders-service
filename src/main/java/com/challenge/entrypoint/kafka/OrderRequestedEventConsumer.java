package com.challenge.entrypoint.kafka;

import com.challenge.domain.orders.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.domain.orders.usecases.CreateOrderUseCase;
import com.challenge.domain.services.CacheService;
import com.challenge.entrypoint.kafka.deserializers.DeserializationException;
import com.challenge.entrypoint.kafka.events.CreateOrderEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static com.challenge.entrypoint.kafka.config.KafkaConsumerConfig.ORDER_REQUESTED_EVENT_FACTORY;

@RequiredArgsConstructor
@Component
public class OrderRequestedEventConsumer {

    @NonNull
    private final OrderRepository orderRepository;
    @NonNull
    private final CacheService cacheService;
    private static final String X_CORRELATION_ID = "X-Correlation-Id";
    private final Logger logger = LogManager.getLogger(this.getClass());

    @KafkaListener(
            topics = "order_requested_event",
            groupId = "orders-service",
            containerFactory = ORDER_REQUESTED_EVENT_FACTORY)
    public void consume(final ConsumerRecord<String, CreateOrderEvent> event, final Acknowledgment acknowledgment) {
        try {
            setCorrelationId(event.headers());
            logger.info("Creating order event received: {}", event);
            var order = event.value();
            if (Objects.isNull(order)) {
                throw new DeserializationException("Error deserializing message");
            }
            new CreateOrderUseCase(cacheService, orderRepository).execute(order.toOrder());
            logger.info("Order created");
        } catch (DeserializationException | DuplicateOrderException e) {
            logger.error("Unrecoverable error processing event: {}", event, e);
        }
        acknowledgment.acknowledge();
    }

    private void setCorrelationId(Headers headers) {
        var correlationIdHeader = headers.lastHeader(X_CORRELATION_ID);
        if (correlationIdHeader != null && correlationIdHeader.value() != null) {
            MDC.put("correlationId", new String(correlationIdHeader.value()));
            return;
        }
        MDC.put("correlationId", UUID.randomUUID().toString());
    }
}
