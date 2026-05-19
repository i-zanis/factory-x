package com.factoryx.catalog.product;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class PriceHistoryEventListener {

    private final PriceHistoryRepository priceHistoryRepository;

    @EventListener
    @Transactional
    public void onProductCreated(ProductCreatedEvent event) {
        PriceHistory history = PriceHistory.record(event.productId(), event.initialPrice());
        priceHistoryRepository.save(history);
    }

    @EventListener
    @Transactional
    public void onProductPriceChanged(ProductPriceChangedEvent event) {
        PriceHistory history = PriceHistory.record(event.productId(), event.newPrice());
        priceHistoryRepository.save(history);
    }
}
