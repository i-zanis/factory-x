package com.izanis.productservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.izanis.productservice.entity.Product;
import com.izanis.productservice.repository.ProductRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  public static final UUID ID = UUID.fromString("e3d3e3d3-c3c3-4343-a3a3-f3d3d3d3d3d1");
  public static final UUID NON_EXISTENT_ID =
      UUID.fromString("e3d3e3d3-c3c3-4343-a4a4-f3d3d3d3d3d1");
  static Product PRODUCT = ProductMother.basic().build();
  static Product PRODUCT_NO_ID = ProductMother.basic().id(null).build();
  @Mock
  ProductRepository productRepository;
  @InjectMocks ProductServiceImpl productService;

  public static Stream<Arguments> findAllDataProvider() {
    return Stream.of(of(List.of(PRODUCT)), of(Collections.emptyList()));
  }

  public static Stream<Arguments> findByIdDataProvider() {
    return Stream.of(
        of(ID, PRODUCT, null),
        of(NON_EXISTENT_ID, null, null),
        of(null, null, IllegalArgumentException.class));
  }

  public static Stream<Arguments> saveDataProvider() {
    return Stream.of(
        of(PRODUCT_NO_ID, PRODUCT, null), of(null, null, IllegalArgumentException.class));
  }

  public static Stream<Arguments> updateDataProvider() {
    return Stream.of(
        of(ID, PRODUCT, null),
        of(ID, null, IllegalArgumentException.class),
        of(UUID.randomUUID(), PRODUCT, IllegalArgumentException.class),
        of(null, PRODUCT, IllegalArgumentException.class));
  }

  public static Stream<Arguments> deleteByIdDataProvider() {
    return Stream.of(of(ID, null), of(null, IllegalArgumentException.class));
  }

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @ParameterizedTest
  @MethodSource("findAllDataProvider")
  void findAll(List<Product> expectedProducts) {
    when(productRepository.findAll()).thenReturn(expectedProducts);
    List<Product> products = productService.findAll();
    assertThat(products).isEqualTo(expectedProducts);
    verify(productRepository, only()).findAll();
  }

  @ParameterizedTest
  @MethodSource("findByIdDataProvider")
  void findById(UUID id, Product expectedProduct, Class<Exception> exceptionClass) {
    if (exceptionClass != null) {
      verify(productRepository, never()).findById(id);
      assertThatExceptionOfType(exceptionClass).isThrownBy(() -> productService.findById(id));
    } else {
      when(productRepository.findById(id)).thenReturn(Optional.ofNullable(expectedProduct));
      Product actualProduct = productService.findById(id).orElse(null);
      assertThat(actualProduct).isEqualTo(expectedProduct);
      verify(productRepository, only()).findById(id);
    }
  }

  @ParameterizedTest
  @MethodSource("saveDataProvider")
  void save(Product product, Product expectedProduct, Class<Exception> exceptionClass) {
    if (exceptionClass != null) {
      assertThatExceptionOfType(exceptionClass).isThrownBy(() -> productService.save(product));
      verify(productRepository, never()).save(product);
    } else {
      assertThat(product.getId()).isNull();
      when(productRepository.save(product)).thenReturn(expectedProduct);
      Product actualProduct = productService.save(product);
      assertThat(actualProduct.getId()).isNotNull();
      assertThat(actualProduct).isEqualTo(expectedProduct);
      verify(productRepository, only()).save(product);
    }
  }

  @ParameterizedTest
  @MethodSource("updateDataProvider")
  void update(UUID id, Product product, Class<Exception> exceptionClass) {
    if (exceptionClass != null) {
      assertThatExceptionOfType(exceptionClass)
          .isThrownBy(() -> productService.update(id, product));
      verify(productRepository, never()).save(product);
    } else {
      when(productRepository.save(product)).thenReturn(product);
      productService.update(id, product);
      verify(productRepository, only()).save(product);
    }
  }

  @ParameterizedTest
  @MethodSource("deleteByIdDataProvider")
  void deleteById(UUID id, Class<Exception> exceptionClass) {
    if (exceptionClass != null) {
      assertThatExceptionOfType(exceptionClass).isThrownBy(() -> productService.deleteById(id));
      verify(productRepository, never()).deleteById(id);
    } else {
      doNothing().when(productRepository).deleteById(id);
      productService.deleteById(id);
      verify(productRepository, only()).deleteById(id);
    }
  }
}
