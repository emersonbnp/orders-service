package com.challenge.infrastructure.data;

import com.challenge.infrastructure.data.entities.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.challenge.infrastructure.data.OrderFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestMongoOrderRepository {

    private final MongoOrderDAO orderDAO = mock(MongoOrderDAO.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private MongoOrderRepository mongoOrderRepository = null;

    @BeforeEach
    public void init() {
        mongoOrderRepository = new MongoOrderRepository(orderDAO, mongoTemplate);
    }

    @Test
    public void should_save_order_and_return_entity_uuid() {
        // Given
        var order = OrderFixture.instanceOfOrder();
        when(orderDAO.save(any())).thenReturn(OrderFixture.instanceOfOrderEntity());

        // When
        var orderUuid = mongoOrderRepository.saveOrder(order);

        // Then
        assertEquals(ORDER_UUID, orderUuid);
    }

    @Test
    public void should_save_order_with_correct_values() {
        // Given
        var order = OrderFixture.instanceOfOrder();
        when(orderDAO.save(any())).thenReturn(OrderFixture.instanceOfOrderEntity());

        // When
        mongoOrderRepository.saveOrder(order);

        // Then
        var orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderDAO).save(orderCaptor.capture());
        assertNull(orderCaptor.getValue().getOrderUuid());

        assertEquals(CUSTOMER_UUID, orderCaptor.getValue().getCustomerUuid());
        assertEquals(SELLER_UUID, orderCaptor.getValue().getSellerUuid());

        assertEquals(PRODUCT_UUID, orderCaptor.getValue().getOrderItems().getFirst().getProductUuid());
        assertEquals(QUANTITY, orderCaptor.getValue().getOrderItems().getFirst().getQuantity());
        assertEquals(PRICE, orderCaptor.getValue().getOrderItems().getFirst().getPrice());
    }

    @Test
    public void should_return_order_correctly_mapped_from_entity() {
        // Given
        var orderEntity = OrderFixture.instanceOfOrderEntity();
        when(orderDAO.findById(any())).thenReturn(Optional.of(orderEntity));

        // When
        var order = mongoOrderRepository.getOrderById(UUID.randomUUID().toString());

        // Then
        assertEquals(CUSTOMER_UUID.toString(), order.orElseThrow().getCustomerUuid());
        assertEquals(SELLER_UUID.toString(), order.orElseThrow().getSellerUuid());

        assertEquals(PRODUCT_UUID.toString(), order.orElseThrow().getItems().getFirst().getProductUuid());
        assertEquals(QUANTITY, order.orElseThrow().getItems().getFirst().getQuantity());
        assertEquals(PRICE, order.orElseThrow().getItems().getFirst().getPrice());
    }
}
