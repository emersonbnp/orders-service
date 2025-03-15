package com.challenge.entrypoint.http.responses;

import java.math.BigDecimal;

public record OrderItemDto(String productUuid, Integer quantity, BigDecimal price) {
}
