package com.izanis.productservice.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@SQLDelete(sql = "UPDATE Brand SET is_deleted = true WHERE id=?")
public class Brand extends BaseEntity {}
