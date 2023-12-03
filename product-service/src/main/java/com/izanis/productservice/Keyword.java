package com.izanis.productservice;

import com.izanis.productservice.entity.BaseEntity;
import com.izanis.productservice.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.lang.Nullable;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@SQLDelete(sql = "UPDATE Keyword SET is_deleted = true WHERE id=?")
public class Keyword extends BaseEntity {
  @NotNull
  String term;
  @NotNull Priority priority;
  @Nullable String locale;

  @ManyToMany(mappedBy = "keywords")
  private Set<Product> products;
}
