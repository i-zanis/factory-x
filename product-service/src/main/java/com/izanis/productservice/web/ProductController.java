package com.izanis.productservice.web;

import com.izanis.model.ProductDto;
import com.izanis.productservice.entity.Product;
import com.izanis.productservice.service.ProductService;
import com.izanis.productservice.web.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProductController.PRODUCT_ENDPOINT_V1_PATH)
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  public static final String PRODUCT_ENDPOINT_V1_PATH = "api/v1/product";
  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDto getProduct(@PathVariable UUID id) {
    var x = productService.findById(id);
    return x;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  // TODO(i-zanis): this might require separate input/output dto
  public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
    Product newProduct = productMapper.toProduct(productDto);
    ProductDto savedProductDto = productMapper.toProductDto(productService.save(newProduct));
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", PRODUCT_ENDPOINT_V1_PATH + "/" + savedProductDto.id().toString())
        .body(savedProductDto);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateProduct(UUID id, @Valid @RequestBody ProductDto productDto) {
    Product productToUpdate = productMapper.toProduct(productDto);
    productService.update(id, productToUpdate);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteProduct(@PathVariable UUID id) {
    productService.deleteById(id);
  }

  @GetMapping("/upc/{upc}")
  @ResponseStatus(HttpStatus.OK)
  ProductDto getProductByUpc(@PathVariable String upc) {
    Product retrievedProduct =
        productService.findByUpc(upc).orElseThrow(EntityNotFoundException::new);
    return productMapper.toProductDto(retrievedProduct);
  }
}
