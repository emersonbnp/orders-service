package com.challenge.entrypoint.http.resources;

import com.challenge.domain.orders.models.Order;
import com.challenge.domain.orders.repository.OrderRepository;
import com.challenge.entrypoint.http.responses.GetOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Orders API", description = "Manage orders")
@RestController
@RequestMapping("/v1/orders")
public class OrdersController {

    private final OrderRepository orderRepository;

    public OrdersController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Operation(summary = "Get orders with filter", description = "Retrieve a list of orders")
    @GetMapping
    public ResponseEntity getOrdersByFilter(
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String seller
    ) {
        if (customer == null && seller == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "At least one of customer or seller is required"));
        }
        var orders = orderRepository.getOrderByExample(new Order(customer, seller));
        return ResponseEntity.ok(GetOrderResponse.of(orders));
    }
}
