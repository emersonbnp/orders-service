package com.challenge.entrypoint.kafka.events;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderItemEvent(
        @NotBlank()
        String productUuid,

        @Min(1)
        Integer quantity,

        @Positive()
        BigDecimal price
) {
}
