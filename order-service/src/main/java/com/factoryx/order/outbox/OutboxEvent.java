package com.factoryx.order.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.Instant;

@Entity
@Table(name = "outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private OutboxEventId id;

    @Embedded
    private AggregateType aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "type"))
    private EventType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    public OutboxEvent(AggregateType aggregateType, String aggregateId, EventType type, String payload) {
        this.id = OutboxEventId.generate();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.type = type;
        this.payload = payload;
        this.createdAt = Instant.now();
    }
}
