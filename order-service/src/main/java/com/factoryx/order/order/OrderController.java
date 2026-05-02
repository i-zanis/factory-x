package com.factoryx.order.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody PlaceOrderRequest request) {
        Order order = orderService.placeOrder(request.customerId(), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getOrdersByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(orderQueryService.getOrdersByCustomer(customerId));
    }

    public record PlaceOrderRequest(UUID customerId, List<OrderLineItemRequest> items) {
    }
}
