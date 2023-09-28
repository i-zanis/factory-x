package com.izanis.productservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.izanis.productservice.ProductController.API_V1_URL;

@RestController
@RequestMapping(API_V1_URL)
@Slf4j
@RequiredArgsConstructor
public class ProductController {
  public static final String API_V1_URL = "/api/v1/product";
  private final ProductService productService;


  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDto getProduct(@PathVariable UUID id) {
    return productService.findById(id);
  }

  @PostMapping
  public ProductDto createProduct(@RequestBody ProductDto productDto) {
//    return productService.save(productDto);
    return null;
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateProduct(@PathVariable UUID id,
                            @RequestBody ProductDto productDto) {

  }
}
