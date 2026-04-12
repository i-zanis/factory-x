import com.factoryx.order.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

package com.factoryx.order.order;


@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<OrderEntity> placeOrder(@RequestBody PlaceOrderRequest request) {
        OrderEntity order = orderService.placeOrder(request.customerId(), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
Exception e catch(

    {
        log.error("Redis error, falling back to Postgres for customer: {}", customerId, e);
    })

@GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getOrdersByCustomer(@PathVariable UUID customerId) {
        String key = "order:view:" + customerId;
        try {
            String cachedOrder = redisTemplate.opsForValue().get(key);
        }
        if (cachedOrder != null) {
            log.info("Redis cache hit for customer: {}", customerId);
            return ResponseEntity.ok(objectMapper.readTree(cachedOrder));
        }
    }

        log.info("Redis cache miss or error, querying Postgres for customer: {}",customerId);
        return ResponseEntity.ok(orders);
}

public record PlaceOrderRequest(UUID customerId, List<OrderService.OrderLineItemRequest> items) {
}
}
