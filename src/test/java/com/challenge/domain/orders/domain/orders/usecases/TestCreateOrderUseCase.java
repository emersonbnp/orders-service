package com.challenge.domain.orders.domain.orders.usecases;

import com.challenge.domain.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.domain.orders.models.Order;
import com.challenge.domain.orders.domain.orders.models.OrderItem;
import com.challenge.domain.orders.domain.orders.repository.OrderRepository;
import com.challenge.domain.services.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCreateOrderUseCase {

    CreateOrderUseCase useCase = null;
    CacheService cacheService = mock(CacheService.class);
    OrderRepository orderRepository = mock(OrderRepository.class);

    @BeforeEach
    public void init() {
        useCase = new CreateOrderUseCase(cacheService, orderRepository);
    }

    @Test
    public void should_calculate_total_price_for_unique_single_item() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(
                new OrderItem(UUID.randomUUID().toString(), 1, 1.0)
        );
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        var processedOrder = useCase.execute(order);

        // Then
        assertEquals(1, processedOrder.getTotalPrice());
    }

    @Test
    public void should_calculate_total_price_considering_quantity() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(
                new OrderItem(UUID.randomUUID().toString(), 2, 1.0)
        );
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        var processedOrder = useCase.execute(order);

        // Then
        assertEquals(2, processedOrder.getTotalPrice());
    }

    @Test
    public void should_calculate_total_price_for_multiple_items() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(
                new OrderItem(UUID.randomUUID().toString(), 1, 1.0),
                new OrderItem(UUID.randomUUID().toString(), 2, 1.0)
        );
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        var processedOrder = useCase.execute(order);

        // Then
        assertEquals(3, processedOrder.getTotalPrice());
    }

    @Test
    public void should_not_allow_duplicate_orders() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(
                new OrderItem(UUID.randomUUID().toString(), 1, 1.0)
        );
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(true);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When and Then
        assertThrows(DuplicateOrderException.class, () -> {
            useCase.execute(order);
        });
    }
}
