package com.factoryx.order.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, OutboxEventId> {
    List<OutboxEvent> findTop100ByOrderByCreatedAtAsc();
}
