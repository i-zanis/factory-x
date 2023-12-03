package com.izanis.productservice.client;

import com.izanis.productservice.product.ProductController;
import com.izanis.productservice.product.dto.ProductDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@RequiredArgsConstructor
@SpringBootTest
class ProductClientTest {
  ProductClient productClient;
  ProductDto productDto;

  public ProductClientTest(ProductClient productClient) {
    this.productClient = productClient;
  }

  @BeforeEach
  void setUp() {
    ProductDto productDto = ProductDtoMother.defaultProductDto()
      .build();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getProductById_validProduct_shouldReturnProduct() throws Exception {
    String uri = UriComponentsBuilder.fromHttpUrl(ProductController.API_V1_URL)
      .pathSegment(UUID.randomUUID()
        .toString())
      .toUriString();
    ProductDto productDto1 = productClient.getProductById(UUID.randomUUID());
    assertThat(productDto1).isEqualTo(productDto);
  }
//  @Test
//  void getProductById() throws Exception {
//    String uri = UriComponentsBuilder.fromHttpUrl(ProductController
//    .API_V1_URL)
//      .pathSegment(UUID.randomUUID()
//        .toString())
//      .toUriString();
//    mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
//      .andExpect(status().isOk());
//  }

//  @Test
//  void saveProduct() {
//  }
//
//  @Test
//  void updateProduct() {
//  }
//
//  @Test
//  void setApiHost() {
//  }
//
//  @Test
//  void deleteProduct() {
//  }
//}

//  @Test
//  void getProduct() throws Exception {
//    String uri = UriComponentsBuilder.fromHttpUrl(ProductController
//    .API_V1_URL)
//      .pathSegment(UUID.randomUUID()
//        .toString())
//      .toUriString();
//    mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
//      .andExpect(status().isOk());
//  }
//
//  @Test
//  void createProduct() throws Exception {
//    mockMvc.perform(post(ProductController.API_V1_URL, productDto).accept
//    (MediaType.APPLICATION_JSON))
//      .andExpect(status().isCreated());
//  }
//
//  @Test
//  void updateProduct() throws Exception {
//    mockMvc.perform(put(ProductController.API_V1_URL + UUID.randomUUID(),
//        productDto).accept(MediaType.APPLICATION_JSON))
//      .andExpect(status().isNoContent());
}