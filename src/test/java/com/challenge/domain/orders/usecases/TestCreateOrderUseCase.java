package com.challenge.domain.orders.usecases;

import com.challenge.domain.orders.exceptions.DuplicateOrderException;
import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.models.OrderItem;
import com.challenge.domain.orders.models.OrderStatusEnum;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.domain.services.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCreateOrderUseCase {

    @InjectMocks
    CreateOrderUseCase useCase;

    @Mock
    CacheService cacheService;

    @Mock
    OrderRepository orderRepository;

    @Test
    public void should_calculate_total_price_for_unique_single_item() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, BigDecimal.ONE));
        var order = new Order(customerUuid, sellerUuid, OrderStatusEnum.CREATED, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());

        assertEquals(BigDecimal.ONE, orderCaptor.getValue().getTotalPrice());
    }

    @Test
    public void should_calculate_total_price_considering_quantity() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 2, BigDecimal.ONE));
        var order = new Order(customerUuid, sellerUuid, OrderStatusEnum.CREATED, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());

        assertEquals(BigDecimal.TWO, orderCaptor.getValue().getTotalPrice());
    }

    @Test
    public void should_calculate_total_price_for_multiple_items() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, BigDecimal.ONE), new OrderItem(UUID.randomUUID().toString(), 2, BigDecimal.ONE));
        var order = new Order(customerUuid, sellerUuid, OrderStatusEnum.CREATED, items);
        var orderUuid = UUID.randomUUID().toString();
        when(cacheService.exists(any())).thenReturn(false);
        when(orderRepository.saveOrder(any())).thenReturn(orderUuid);

        // When
        useCase.execute(order);

        // Then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveOrder(orderCaptor.capture());

        assertEquals(BigDecimal.valueOf(3), orderCaptor.getValue().getTotalPrice());
    }

    @Test
    public void should_not_allow_duplicate_orders() {
        // Given
        var customerUuid = UUID.randomUUID().toString();
        var sellerUuid = UUID.randomUUID().toString();
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, BigDecimal.ONE));
        var order = new Order(customerUuid, sellerUuid, OrderStatusEnum.CREATED, items);
        when(cacheService.exists(any())).thenReturn(true);

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
        var items = List.of(new OrderItem(UUID.randomUUID().toString(), 1, BigDecimal.ONE));
        var order = new Order(customerUuid, sellerUuid, OrderStatusEnum.CREATED, items);
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
