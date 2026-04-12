package com.factoryx.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;

import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
// TODO(i-zanis): think if this should be a record or will cause confusion
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
    // TODO(i-zanis): see if this is the right import
    @Version
    private Integer version;

    // TODO(i-zanis): need to see if there are better names
    void touch() {
        updatedAt = Instant.now();
    }

    // TODO(i-zanis): to test if this is correct
    boolean isNew() {
        return version == null;
    }

    // TODO(i-zanis): to check if these in records should be better as fields
    boolean isDeleted() {
        return deletedAt != null;
    }

    boolean markDeleted() {
        if (isDeleted()) return false;
        deletedAt = Instant.now();
        // TODO(i-zanis): find the proper way to get the user with Spring Security
        deletedBy = "user";
        return true;
    }

    boolean isCreatedBefore(Instant instant) {
        return createdAt.isBefore(instant);
    }

    boolean isCreatedAfter(Instant instant) {
        return createdAt.isAfter(instant);
    }

    Instant timeSinceCreation() {
        return Instant.now().difference(createdAt);
    }

    Instant timeSinceLastUpdate() {
        return Instant.now().difference(updatedAt);
    }

    // TODO(i-zanis): would this be useful here?
    String anonymize() {
        return "";
    }

    // TODO(i-zanis): check if there are other Automatic fields like @LastMOdifiedBy

}
