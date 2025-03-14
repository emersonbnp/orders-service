package com.challenge.entrypoint.kafka.events;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemEvent(
        @NotBlank()
        String productUuid,

        @Min(1)
        Integer quantity,

        @Positive()
        Double price
) {
}
