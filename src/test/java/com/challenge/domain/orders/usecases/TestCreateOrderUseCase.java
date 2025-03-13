package com.challenge.domain.orders.usecases;

import com.challenge.domain.orders.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.domain.services.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, 1.0));
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());

        assertEquals(1, orderCaptor.getValue().getTotalPrice());
    }

    @Test
    public void should_calculate_total_price_considering_quantity() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 2, 1.0));
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());

        assertEquals(2, orderCaptor.getValue().getTotalPrice());
    }

    @Test
    public void should_calculate_total_price_for_multiple_items() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, 1.0), new OrderItem(UUID.randomUUID().toString(), 2, 1.0));
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());

        assertEquals(3, orderCaptor.getValue().getTotalPrice());
    }

    @Test
    public void should_not_allow_duplicate_orders() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, 1.0));
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(true);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When and Then
        assertThrows(DuplicateOrderException.class, () -> {
            useCase.execute(order);
        });
    }

    @Test
    public void should_set_a_duplication_check() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, 1.0));
        var order = new Order(customerUuid, sellerUuid, items);
        var orderUuid = UUID.randomUUID().toString();
        var expectedDuplicationKey = String.format("%s.%s", customerUuid, order.hashCode());
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(cacheService).set(keyCaptor.capture());

        assertEquals(expectedDuplicationKey, keyCaptor.getValue());
    }
}
