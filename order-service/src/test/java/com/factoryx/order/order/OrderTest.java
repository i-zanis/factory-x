package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

class OrderTest {

    @Nested
    class LineItems {

        @Test
        void addLineItem_NewSku_AddsNewItem() {
            Order sut = OrderMother.pending().build();
            Sku newSku = new Sku("AAA-1234");

            sut.addLineItem(ProductId.generate(), newSku, new Quantity(2), new Money(50.0));

            assertThat(sut.getLineItems())
                    .hasSize(1)
                    .first()
                    .returns(newSku, OrderLineItem::getSku)
                    .returns(new Quantity(2), OrderLineItem::getQuantity)
                    .returns(new Money(100.0), OrderLineItem::subtotal);
        }

        @Test
        void addLineItem_ExistingSku_IncrementsQuantity() {
            Order sut = OrderMother.withLineItem("EXT-1234").build();

            sut.addLineItem(ProductId.generate(), new Sku("EXT-1234"), new Quantity(2), new Money(100.0));

            assertThat(sut.getLineItems())
                    .hasSize(1)
                    .first()
                    .returns(new Quantity(3), OrderLineItem::getQuantity)
                    .returns(new Money(300.0), OrderLineItem::subtotal);
        }

        @Test
        void addLineItem_NotPending_ThrowsException() {
            Order sut = OrderMother.placed().build();

            assertThatThrownBy(() -> sut.addLineItem(ProductId.generate(), new Sku("ANY-1234"), new Quantity(1), new Money(100.0)))
                    .isInstanceOf(DomainRuleViolation.class)
                    .hasMessageContaining("Cannot add items to PLACED order");
        }
    }

    @Nested
    class StateTransitions {

        @Test
        void place_EmptyOrder_ThrowsDomainRuleViolation() {
            Order sut = OrderMother.pending().build();

            assertThatThrownBy(sut::place)
                    .isInstanceOf(DomainRuleViolation.class)
                    .hasMessageContaining("Cannot place empty order");
        }

        @Test
        void place_ValidOrder_TransitionsAndCalculatesTotalAndEmitsEvent() {
            Order sut = OrderMother.withLineItem("AAA-1234").build();
            sut.addLineItem(ProductId.generate(), new Sku("BBB-5678"), new Quantity(2), new Money(50.0));

            sut.place();

            assertThat(sut)
                    .returns(OrderStatus.PLACED, Order::getStatus)
                    .returns(new Money(200.0), Order::getTotalPrice);
            assertThat((Collection<?>) invokeMethod(sut, "domainEvents"))
                    .hasAtLeastOneElementOfType(OrderCreatedEvent.class)
                    .first()
                    .satisfies(e -> {
                        OrderCreatedEvent event = (OrderCreatedEvent) e;
                        assertThat(event.totalPrice()).isEqualTo(new Money(200.0));
                        assertThat(event.lineItems()).hasSize(2);
                    });
        }

        @Test
        void approve_FromPlaced_TransitionsToApproved() {
            Order sut = OrderMother.placed().build();

            sut.approve();

            assertThat(sut.getStatus()).isEqualTo(OrderStatus.APPROVED);
        }

        @Test
        void fulfill_FromPlaced_ThrowsException() {
            Order sut = OrderMother.placed().build();

            assertThatThrownBy(sut::fulfill)
                    .isInstanceOf(DomainRuleViolation.class)
                    .hasMessageContaining("Cannot transition from PLACED to FULFILLED");
        }

        @Test
        void fulfill_FromApproved_TransitionsToFulfilled() {
            Order sut = OrderMother.approved().build();

            sut.fulfill();

            assertThat(sut.getStatus()).isEqualTo(OrderStatus.FULFILLED);
        }
    }
}
