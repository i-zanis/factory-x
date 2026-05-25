package com.factoryx.order.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    // TODO this is primitive
    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "aggregate_type", nullable = false))
    private AggregateType aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "type", nullable = false))
    private EventType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    public OutboxEvent(AggregateType aggregateType, String aggregateId, EventType type, String payload) {
        this.id = UUID.randomUUID();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.type = type;
        this.payload = payload;
        this.createdAt = Instant.now();
    }

    public static OutboxEvent from(AggregateType aggregateType, String aggregateId, EventType type, String payload) {
        return new OutboxEvent(aggregateType, aggregateId, type, payload);
    }
}
