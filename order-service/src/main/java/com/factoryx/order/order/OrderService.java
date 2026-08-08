package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import com.factoryx.order.infrastructure.ServiceUnavailableException;
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
    private final ProductPriceProvider priceProvider;

    @Transactional
    @CircuitBreaker(name = "catalogService", fallbackMethod = "placeOrderFallback")
    public Order placeOrder(CustomerId customerId, List<OrderLineItemRequest> requests) {
        var order = Order.create(customerId);

        var skus = requests.stream()
                .map(req -> new Sku(req.sku()))
                .toList();

        var priceMap = priceProvider.getPriceInfos(skus);

        for (var req : requests) {
            var sku = new Sku(req.sku());
            var pricedItem = priceMap.get(sku);

            if (pricedItem == null || !pricedItem.exists()) {
                throw new DomainRuleViolation("SKU not found in catalog: " + sku.value());
            }

            order.addLineItem(
                    new ProductId(req.productId()),
                    sku,
                    new Quantity(req.quantity()),
                    pricedItem.price()
            );
        }

        order.place();

        return orderRepository.save(order);
    }

    public Order placeOrderFallback(CustomerId customerId, List<OrderLineItemRequest> requests, Throwable t) {
        log.error("Circuit breaker 'catalogService' triggered during placeOrder for customer: {}", customerId.value(), t);
        throw new ServiceUnavailableException("Catalog service is currently unavailable. Please try again later.", t);
    }
}
