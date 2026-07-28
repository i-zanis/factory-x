package com.factoryx.order.order;

import com.factoryx.order.application.OrderDtoMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        var order = orderService.placeOrder(new CustomerId(request.customerId()), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDtoMapper.toDto(order));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getOrdersByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(orderQueryService.getOrdersByCustomer(new CustomerId(customerId)));
    }

    @Builder
    public record PlaceOrderRequest(
            @NotNull UUID customerId,
            @NotEmpty List<@Valid OrderLineItemRequest> items) {
    }
}