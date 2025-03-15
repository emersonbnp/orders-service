package com.challenge.entrypoint.http.responses;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        String customerUuid,
        String sellerUuid,
        String status,
        List<OrderItemDto> items,
        BigDecimal totalPrice
) {
}

