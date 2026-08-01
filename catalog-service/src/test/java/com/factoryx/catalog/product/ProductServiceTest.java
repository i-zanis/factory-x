package com.factoryx.catalog.product;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.factoryx.catalog.product.ProductMother.custom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService sut;

    private Product product;
    private ProductId productId;

    private static Stream<DiscountScenario> discountScenarios() {
        return Stream.of(
                new DiscountScenario(10, "90.00"),
                new DiscountScenario(25, "75.00"),
                new DiscountScenario(50, "50.00")
        );
    }

    @BeforeEach
    void setUp() {
        product = custom().build();
        productId = product.getId();
    }

    @Test
    void createProduct_SavesAndReturnsProduct() {
        Sku sku = new Sku("NEW-1234");
        String name = "New Name";
        Money price = new Money(200.0);
        given(productRepository.save(any(Product.class))).willAnswer(i -> i.getArgument(0));

        Product actual = sut.createProduct(sku, name, price);

        assertThat(actual).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updatePrice_ExistingProduct_UpdatesAndSaves() {
        Money newPrice = new Money(500.0);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.save(product)).willReturn(product);

        Product actual = sut.updatePrice(productId, newPrice);

        assertThat(actual.getPrice()).isEqualTo(newPrice);
        verify(productRepository).save(product);
    }

    @Test
    void updatePrice_NotFound_ThrowsException() {
        Money newPrice = new Money(500.0);
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.updatePrice(productId, newPrice))
                .isInstanceOf(DomainRuleViolation.class)
                .hasMessageContaining("Product not found");
    }

    @ParameterizedTest
    @MethodSource("discountScenarios")
    void applyDiscount_ValidScenarios_AppliesAndSaves(DiscountScenario scenario) {
        Product p = custom().sku(new Sku("AAA-1234")).name("Test").price(new Money(100.0)).build();
        given(productRepository.findById(p.getId())).willReturn(Optional.of(p));
        given(productRepository.save(p)).willReturn(p);

        Product actual = sut.applyDiscount(p.getId(), scenario.percent());

        assertThat(actual.getPrice().amount()).isEqualByComparingTo(scenario.expectedAmount());
        verify(productRepository).save(p);
    }

    @Test
    void rename_ValidName_RenamesAndSaves() {
        ProductName newName = new ProductName("Updated Name");
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.save(product)).willReturn(product);

        Product actual = sut.rename(productId, newName);

        assertThat(actual.getName()).isEqualTo(newName);
        verify(productRepository).save(product);
    }

    record DiscountScenario(int percent, String expectedAmount) {}
}
