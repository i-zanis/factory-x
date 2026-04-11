package com.factoryx.order.order;

import com.factoryx.catalog.grpc.InternalCatalogServiceGrpc;
import com.factoryx.order.outbox.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @GrpcClient("catalog-service")
    private InternalCatalogServiceGrpc.InternalCatalogServiceBlockingStub catalogStub;

    @Transactional
    public OrderEntity placeOrder(UUID customerId, List<OrderLineItemRequest> requests) {
        OrderEntity order = new OrderEntity(customerId);
        
        List<OrderLineItemEntity> items = requests.stream()
                .map(req -> new OrderLineItemEntity(req.productId(), req.sku(), req.quantity()))
                .collect(Collectors.toList());

        order.place(items, catalogStub);
        
        OrderEntity savedOrder = orderRepository.save(order);
        outboxRepository.save(savedOrder.toOutboxEvent(objectMapper));

        return savedOrder;
    }

    public record OrderLineItemRequest(UUID productId, String sku, int quantity) {}
}
