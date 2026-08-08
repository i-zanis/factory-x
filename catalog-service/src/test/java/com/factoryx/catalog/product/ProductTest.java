package com.factoryx.catalog.product;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collection;
import java.util.Currency;

import static com.factoryx.catalog.product.ProductMother.custom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

@TestClassOrder(ClassOrderer.ClassName.class)
class ProductTest {

    private static final Currency USD = Currency.getInstance("USD");

    @Nested
    class Creation {
        @Test
        void validInputs_CreatesProductWithAuditInfo() {
            Product sut = custom().build();

            Collection<?> actualEvents = invokeMethod(sut, "domainEvents");

            assertThat(sut.getAuditInfo()).isNotNull();
            assertThat(actualEvents).hasAtLeastOneElementOfType(ProductCreatedEvent.class);
        }
    }

    @Nested
    class PriceUpdates {
        @Test
        void validMoney_UpdatesPriceAndEmitsEvent() {
            Product sut = custom().build();
            invokeMethod(sut, "clearDomainEvents");
            Money newPrice = new Money(150.0);

            sut.updatePrice(newPrice);

            assertThat(sut).returns(newPrice, Product::getPrice);
            assertThat((Collection<?>) invokeMethod(sut, "domainEvents")).hasAtLeastOneElementOfType(ProductPriceChangedEvent.class);
        }

        @Test
        void zeroOrNegative_ThrowsDomainRuleViolation() {
            Product sut = custom().build();
            Money invalidPrice = Money.zero(USD);

            assertThatThrownBy(() -> sut.updatePrice(invalidPrice))
                    .isInstanceOf(DomainRuleViolation.class)
                    .hasMessageContaining("must be > 0");
        }
    }

    @Nested
    class Discounts {
        @ParameterizedTest(name = "Discounting {0}% yields {1}")
        @CsvSource({"10, 90.00", "50, 50.00", "1, 99.00"})
        void validPercentages_AdjustsPriceCorrectly(int percent, String expectedAmount) {
            Product sut = custom().build();

            sut.applyDiscount(percent);

            assertThat(sut.getPrice().amount()).isEqualByComparingTo(expectedAmount);
        }

        @ParameterizedTest(name = "Invalid discount {0}% throws DomainRuleViolation")
        @CsvSource({"0", "51", "-5"})
        void invalidPercentages_ThrowsException(int percent) {
            Product sut = custom().build();

            assertThatThrownBy(() -> sut.applyDiscount(percent))
                    .isInstanceOf(DomainRuleViolation.class);
        }
    }
}
