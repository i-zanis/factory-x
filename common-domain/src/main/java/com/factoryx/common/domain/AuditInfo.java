package com.factoryx.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class AuditInfo {

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Column(updatable = false)
    private Instant deletedAt;

    @Column(updatable = false)
    private String deletedBy;
    
    @Version
    private Integer version;

    public void touch() {
        updatedAt = Instant.now();
    }

    public boolean isNew() {
        return version == null;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean markDeleted(String user) {
        if (isDeleted()) return false;
        deletedAt = Instant.now();
        deletedBy = user;
        return true;
    }

    public boolean isCreatedBefore(@NonNull Instant instant) {
        if (createdAt == null) return false;
        return createdAt.isBefore(instant);
    }

    public boolean isCreatedAfter(@NonNull Instant instant) {
        if (createdAt == null) return false;
        return createdAt.isAfter(instant);
    }

    public Duration timeSinceCreation() {
        if (createdAt == null) return Duration.ZERO;
        return Duration.between(createdAt, Instant.now());
    }

    public Duration timeSinceLastUpdate() {
        if (updatedAt == null) return Duration.ZERO;
        return Duration.between(updatedAt, Instant.now());
    }
}
