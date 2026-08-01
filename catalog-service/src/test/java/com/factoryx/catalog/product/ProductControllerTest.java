package com.factoryx.catalog.product;

import com.factoryx.catalog.model.ProductRequest;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.factoryx.catalog.product.ProductMother.custom;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc sut;
    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @Mock
    private ProductQueryService productQueryService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        sut = MockMvcBuilders.standaloneSetup(new ProductController(productService, productQueryService)).build();
    }

    @Test
    void createProduct_Returns201AndDto() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .sku("SKU-1234")
                .name("New Product")
                .price(100.50)
                .build();
        Product product = custom().sku(new Sku("SKU-1234")).name("New Product").price(new Money(100.50)).build();
        given(productService.createProduct(eq(new Sku("SKU-1234")), eq("New Product"), any(Money.class))).willReturn(product);

        ResultActions actual = sut.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        actual.andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-1234"))
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void getProductById_Found_Returns200() throws Exception {
        Product product = custom().build();
        given(productQueryService.getProductById(product.getId())).willReturn(Optional.of(product));

        ResultActions actual = sut.perform(get("/products/" + product.getId().value()));

        actual.andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value(product.getSku().value()));
    }

    @Test
    void getProductById_NotFound_Returns404() throws Exception {
        ProductId id = ProductId.generate();
        given(productQueryService.getProductById(id)).willReturn(Optional.empty());

        ResultActions actual = sut.perform(get("/products/" + id.value()));

        actual.andExpect(status().isNotFound());
    }
}
