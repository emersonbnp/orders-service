package com.challenge.entrypoint.http.responses;

import com.challenge.domain.orders.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GetOrderResponse {

    public List<OrderDto> elements;

    public static GetOrderResponse of(List<Order> orders) {
        var elements = orders.stream().map(model -> new OrderDto(
                model.getCustomerUuid(),
                model.getSellerUuid(),
                model.getStatus().name(),
                model.getItems().stream().map(item -> new OrderItemDto(
                        item.getProductUuid(),
                        item.getQuantity(),
                        item.getPrice()
                )).toList(),
                model.getTotalPrice()
        )).toList();
        return new GetOrderResponse(elements);
    }
}
