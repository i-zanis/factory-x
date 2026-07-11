package com.factoryx.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class AuditInfo {

    @CreatedDate
    @Column(updatable = false)
    @Nullable
    private Instant createdAt;

    @LastModifiedDate
    @Nullable
    private Instant updatedAt;

    @CreatedBy
    @Column(updatable = false)
    @Nullable
    private String createdBy;

    @LastModifiedBy
    @Nullable
    private String updatedBy;

    @Nullable
    private Instant deletedAt;

    @Nullable
    private String deletedBy;

    @Version
    @Nullable
    private Integer version;

    public boolean isNew() {
        return version == null;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted(String user) {
        Require.text(user, "User performing deletion");
        deletedAt = Instant.now();
        deletedBy = user;
    }

    public boolean isCreatedBefore(Instant instant) {
        Require.nonNull(instant, "Instant for comparison");
        if (createdAt == null) return false;
        return createdAt.isBefore(instant);
    }

    public boolean isCreatedAfter(Instant instant) {
        Require.nonNull(instant, "Instant for comparison");
        if (createdAt == null) return false;
        return createdAt.isAfter(instant);
    }

    public boolean isUpdatedAfter(Instant instant) {
        Require.nonNull(instant, "Instant for comparison");
        if (updatedAt == null) return false;
        return updatedAt.isAfter(instant);
    }

    public Duration timeSinceCreation() {
        if (createdAt == null) return Duration.ZERO;
        return Duration.between(createdAt, Instant.now());
    }

    public Duration timeSinceLastUpdate() {
        if (updatedAt == null) return Duration.ZERO;
        return Duration.between(updatedAt, Instant.now());
    }

    public boolean isModified() {
        if (createdAt == null || updatedAt == null) return false;
        return updatedAt.isAfter(createdAt);
    }

    public List<String> toAuditTrail() {
        return List.of(
            "Created by: " + (createdBy != null ? createdBy : "Unknown") + " at " + createdAt,
            "Updated by: " + (updatedBy != null ? updatedBy : "Unknown") + " at " + updatedAt
        );
    }
}
