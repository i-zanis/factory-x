package com.factoryx.catalog.domain;

import java.time.Instant;

public record AuditInfo(Instant createdAt, Instant updatedAt, String createdBy, String updatedBy) {
}
