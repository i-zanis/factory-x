package com.factoryx.catalog.product;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductSpecsTest {

    @Test
    void nameContains_WithValidKeyword_ReturnsSpecification() {
        Specification<Product> spec = ProductSpecs.nameContains("Widget");
        assertThat(spec).isNotNull();
    }

    @Test
    void nameContains_WithNullOrBlank_ReturnsNull() {
        assertThat(ProductSpecs.nameContains(null)).isNull();
        assertThat(ProductSpecs.nameContains("   ")).isNull();
    }

    @Test
    void priceBetween_WithValidRange_ReturnsSpecification() {
        Specification<Product> spec = ProductSpecs.priceBetween(BigDecimal.TEN, BigDecimal.valueOf(100));
        assertThat(spec).isNotNull();
    }

    @Test
    void priceBetween_WithBothNull_ReturnsNull() {
        assertThat(ProductSpecs.priceBetween(null, null)).isNull();
    }
}
