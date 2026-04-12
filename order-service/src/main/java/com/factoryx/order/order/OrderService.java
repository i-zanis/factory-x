package com.factoryx.order.order;

import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductPriceProvider priceProvider;

    @Transactional
    @CircuitBreaker(name = "catalogService", fallbackMethod = "placeOrderFallback")
    public Order placeOrder(UUID customerId, List<OrderLineItemRequest> requests) {
        Order order = new Order(customerId);

        List<OrderLineItem> items = requests.stream()
                .map(req -> new OrderLineItem(
                        req.productId(),
                        new Sku(req.sku()),
                        new Quantity(req.quantity())
                ))
                .collect(Collectors.toList());

        order.place(items, priceProvider);

        return orderRepository.save(order);
    }

    public Order placeOrderFallback(UUID customerId, List<OrderLineItemRequest> requests, Throwable t) {
        log.error("Circuit breaker 'catalogService' triggered during placeOrder for customer: {}", customerId, t);
        throw new RuntimeException("Catalog service is currently unavailable. Please try again later.", t);
    }

    // TODO(i-zanis): think if needs moving to new file
    public record OrderLineItemRequest(UUID productId, String sku, int quantity) {}
}
