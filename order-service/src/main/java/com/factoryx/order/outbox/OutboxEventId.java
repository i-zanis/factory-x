package com.factoryx.order.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OutboxEventId implements Serializable {
    @Column(name = "id")
    private UUID value;

    public static OutboxEventId generate() {
        return new OutboxEventId(UUID.randomUUID());
    }
}
