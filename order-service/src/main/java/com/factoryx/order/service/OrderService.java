package com.factoryx.order.service;

import com.factoryx.catalog.grpc.InternalCatalogServiceGrpc;
import com.factoryx.catalog.grpc.PriceRequest;
import com.factoryx.catalog.grpc.PriceResponse;
import com.factoryx.order.domain.OrderStatus;
import com.factoryx.order.persistence.OrderEntity;
import com.factoryx.order.persistence.OrderLineItemEntity;
import com.factoryx.order.persistence.OrderRepository;
import com.factoryx.order.persistence.OutboxEventEntity;
import com.factoryx.order.persistence.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private InternalCatalogServiceGrpc.InternalCatalogServiceBlockingStub catalogStub;

    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        catalogStub = InternalCatalogServiceGrpc.newBlockingStub(channel);
    }

    @Transactional
    @SneakyThrows
    public OrderEntity placeOrder(UUID customerId, List<OrderLineItemRequest> requests) {
        OrderEntity order = new OrderEntity(UUID.randomUUID(), customerId, OrderStatus.PENDING);

        List<OrderLineItemEntity> validatedItems = requests.stream().map(req -> {
            PriceResponse catalogResponse = catalogStub.getProductPrice(
                    PriceRequest.newBuilder().setSku(req.sku()).build()
            );

            if (!catalogResponse.getExists()) {
                throw new IllegalArgumentException("SKU not found in catalog: " + req.sku());
            }

            return new OrderLineItemEntity(req.productId(), req.sku(), req.quantity(), catalogResponse.getPrice());
        }).toList();

        order.setLineItems(validatedItems);
        
        OrderEntity savedOrder = orderRepository.save(order);
        
        OutboxEventEntity outboxEvent = new OutboxEventEntity(
                "Order",
                savedOrder.getId().toString(),
                "OrderCreated",
                objectMapper.writeValueAsString(savedOrder)
        );
        
        outboxRepository.save(outboxEvent);

        return savedOrder;
    }

    public record OrderLineItemRequest(UUID productId, String sku, int quantity) {}
}
