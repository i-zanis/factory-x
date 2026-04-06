package com.factoryx.order.web;

import com.factoryx.order.persistence.OrderEntity;
import com.factoryx.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderEntity> placeOrder(@RequestBody PlaceOrderRequest request) {
        OrderEntity order = orderService.placeOrder(request.customerId(), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    public record PlaceOrderRequest(UUID customerId, List<OrderService.OrderLineItemRequest> items) {}
}
