package com.challenge.entrypoint.http.responses;

public record OrderItemDto(String productUuid, Integer quantity, Double price) {
}
