package com.izanis.productservice.entity;

import com.izanis.productservice.Keyword;
import com.izanis.productservice.upc.ValidUpc;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@SQLDelete(sql = "UPDATE Product SET is_deleted = true WHERE id=?")
public class Product extends BaseEntity {
  @Column(nullable = false)
  @ColumnDefault("0")
  @NotNull
  @PositiveOrZero
  Integer minAvailableInventory;

  @Column(length = 50, nullable = false)
  @ColumnDefault("''")
  @Size(min = 3, max = 50)
  private String name;

  @Column(unique = true, nullable = false)
  @ValidUpc
  private String upc;

  @Enumerated(EnumType.STRING)
  @Nullable
  private Category category;

  @NotNull
  @PositiveOrZero
  @ColumnDefault("0")
  @Column(nullable = false)
  private BigDecimal price;

  @PositiveOrZero
  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer totalInventory;

  @PositiveOrZero
  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer availableInventory;

  //  @Nullable
  private String imageUrl;
  //  @Nullable
  private String description;
  @ManyToOne @Nullable private Brand brand;
  @Nullable private BigDecimal weight;
  @Nullable private String dimensions;
  @Nullable private String manufacturer;
  @Nullable private String countryOfOrigin;
  @ManyToMany private Set<Keyword> keywords;

  @PositiveOrZero
  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer inventoryToManufacture;

  public enum Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    OTHER
  }
}
