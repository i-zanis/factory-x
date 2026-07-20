package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

public class OrderMother {

    @Builder(builderClassName = "OrderBuilder", builderMethodName = "pending")
    static Order create(CustomerId customerId, @Singular List<String> lineItems, OrderStatus targetStatus) {
        Order order = Order.create(customerId != null ? customerId : CustomerId.generate());
        boolean hasItems = false;
        
        if (lineItems != null && !lineItems.isEmpty()) {
            for (String sku : lineItems) {
                order.addLineItem(ProductId.generate(), new Sku(sku), new Quantity(1), new Money(100.0));
            }
            hasItems = true;
        }

        if (targetStatus != null && targetStatus.ordinal() >= OrderStatus.PLACED.ordinal()) {
            if (!hasItems) {
                order.addLineItem(ProductId.generate(), new Sku("AAA-1234"), new Quantity(1), new Money(100.0));
            }
            order.place();
        }
        if (targetStatus != null && targetStatus.ordinal() >= OrderStatus.APPROVED.ordinal()) {
            order.approve();
        }
        if (targetStatus != null && targetStatus.ordinal() >= OrderStatus.FULFILLED.ordinal()) {
            order.fulfill();
        }
        return order;
    }

    public static OrderBuilder withLineItem(String sku) {
        return pending().lineItem(sku);
    }

    public static OrderBuilder placed() {
        return pending().targetStatus(OrderStatus.PLACED);
    }

    public static OrderBuilder approved() {
        return pending().targetStatus(OrderStatus.APPROVED);
    }

    public static class OrderBuilder {}
}
