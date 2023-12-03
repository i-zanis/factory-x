package com.izanis.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Where(clause = "is_deleted = false")
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false, nullable = false)
  private UUID id;

  @CreationTimestamp
  @Column(updatable = false, nullable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(updatable = false, nullable = false)
  private Instant lastModifiedAt;

  @Version
  @ColumnDefault("0")
  private Long version;

  @Column(updatable = false)
  private String createdBy;

  @Column(updatable = false)
  private String modifiedBy;

  @ColumnDefault("false")
  @Column(name = "is_deleted", nullable = false)
  private boolean deleted;

  public boolean isNew() {
    return this.id == null;
  }
}
