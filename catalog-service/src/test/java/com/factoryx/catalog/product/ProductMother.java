package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import lombok.Builder;

public class ProductMother {
    @lombok.Builder(builderClassName = "Builder", builderMethodName = "validProduct", buildMethodName = "build")
    public static Product createTestProduct(Sku sku, String name, Money price) {
        return Product.create(
                sku != null ? sku : new Sku("AAA-1234"),
                name != null ? name : "Test Product",
                price != null ? price : new Money(100.0)
        );
    }

    public static Builder custom() {
        return validProduct();
    }

    public static Builder electronics() {
        return validProduct().sku(new Sku("ELEC-001")).name("Smartphone").price(new Money(899.99));
    }

    public static Builder clothing() {
        return validProduct().sku(new Sku("CLOT-001")).name("T-Shirt").price(new Money(19.99));
    }

    public static Builder food() {
        return validProduct().sku(new Sku("FOOD-001")).name("Apple").price(new Money(1.50));
    }
}
