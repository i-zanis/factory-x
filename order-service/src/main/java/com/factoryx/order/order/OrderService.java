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
    private final ProductPriceProvider priceProvider; // ACL interface

    @Transactional
    @CircuitBreaker(name = "catalogService", fallbackMethod = "placeOrderFallback")
    public Order placeOrder(UUID customerId, List<OrderLineItemRequest> requests) {
        Order order = Order.create(new CustomerId(customerId));

        for (OrderLineItemRequest req : requests) {
            Sku sku = new Sku(req.sku());
            ProductPriceProvider.PriceInfo priceInfo = priceProvider.getPriceInfo(sku);
            
            if (!priceInfo.exists()) {
                throw new IllegalArgumentException("SKU not found in catalog: " + sku.value());
            }

            OrderLineItem item = new OrderLineItem(
                    new ProductId(req.productId()),
                    sku,
                    Quantity.of(req.quantity()),
                    priceInfo.price()
            );
            order.addLineItem(item);
        }

        order.place();

        return orderRepository.save(order);
    }

    public Order placeOrderFallback(UUID customerId, List<OrderLineItemRequest> requests, Throwable t) {
        log.error("Circuit breaker 'catalogService' triggered during placeOrder for customer: {}", customerId, t);
        throw new RuntimeException("Catalog service is currently unavailable. Please try again later.", t);
    }
}
