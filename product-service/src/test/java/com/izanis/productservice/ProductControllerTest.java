package com.izanis.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izanis.productservice.product.ProductController;
import com.izanis.productservice.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductControllerTest.class)
@RequiredArgsConstructor
class ProductControllerTest {

  MockMvc mockMvc;

  ObjectMapper mapper;

  ProductDto productDto;
  @BeforeEach
  void setUp() {
    productDto = ProductDto.builder()
      .build();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getProduct() throws Exception {
    String uri = UriComponentsBuilder.fromHttpUrl(ProductController.API_V1_URL)
      .pathSegment(UUID.randomUUID()
        .toString())
      .toUriString();
    mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void createProduct() throws Exception {
    mockMvc.perform(post(ProductController.API_V1_URL, productDto).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
  }

  @Test
  void updateProduct() throws Exception {
    mockMvc.perform(put(ProductController.API_V1_URL + UUID.randomUUID(),
        productDto).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

//  void deleteProduct throws Exception() {
//    client.deleteProduct
//  }
}