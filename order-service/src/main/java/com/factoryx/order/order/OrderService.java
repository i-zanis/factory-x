package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductPriceProvider priceProvider; // ACL interface

    @Transactional
    @CircuitBreaker(name = "catalogService", fallbackMethod = "placeOrderFallback")
    public Order placeOrder(CustomerId customerId, List<OrderLineItemRequest> requests) {
        Order order = Order.create(customerId);

        // TODO(DDD-Blueprint): Fixed N+1 RPC Problem by using batch lookup.
        // A: Confirmed. Batch request implemented below.
        // A2: Should also add pagination/chunking if 'requests' list is very large to avoid gRPC message size limits.
        List<Sku> skus = requests.stream()
                .map(req -> new Sku(req.sku()))
                .toList();

        Map<Sku, ProductPriceProvider.PriceInfo> priceMap = priceProvider.getPriceInfos(skus);

        for (OrderLineItemRequest req : requests) {
            Sku sku = new Sku(req.sku());
            ProductPriceProvider.PriceInfo priceInfo = priceMap.get(sku);
            
            if (priceInfo == null || !priceInfo.exists()) {
                throw new DomainRuleViolation("SKU not found in catalog: " + sku.value());
            }

            order.addLineItem(
                    new ProductId(req.productId()),
                    sku,
                    new Quantity(req.quantity()),
                    priceInfo.price()
            );
        }

        order.place();

        return orderRepository.save(order);
    }

    public Order placeOrderFallback(CustomerId customerId, List<OrderLineItemRequest> requests, Throwable t) {
        log.error("Circuit breaker 'catalogService' triggered during placeOrder for customer: {}", customerId.value(), t);
        // TODO: Use a specific exception (e.g. ServiceUnavailableException) instead of generic RuntimeException.
        // A2: Spring's @ResponseStatus could then be placed on that new exception to automatically map to 503.
        throw new RuntimeException("Catalog service is currently unavailable. Please try again later.", t);
    }
}
