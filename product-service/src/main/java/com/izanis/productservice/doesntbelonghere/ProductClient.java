package com.izanis.productservice.doesntbelonghere;

import com.izanis.model.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Component
@ConfigurationProperties(value = "izanis.com", ignoreUnknownFields = false)
@Slf4j
public class ProductClient {
  public final String PRODUCT_PATH_V1 = "api/v1/product";
  public final String CUSTOMER_PATH_V1 = "api/v1/customer";
  private final RestTemplate restTemplate;
  private String apiHost;

  public ProductClient(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public ProductDto getProductById(UUID id) {
    return restTemplate.getForObject(buildUri(id), ProductDto.class);
  }

  public URI saveProduct(ProductDto productDto) {
    return restTemplate.postForLocation(apiHost + PRODUCT_PATH_V1, productDto);
  }

  public void updateProduct(UUID id, ProductDto productDto) {
    restTemplate.put(buildUri(id), productDto);
  }

  private String buildUri(UUID id) {
    return UriComponentsBuilder.fromHttpUrl(apiHost)
      .pathSegment(PRODUCT_PATH_V1)
      .pathSegment("/")
      .pathSegment(id.toString())
      .toUriString();
  }

  public void setApiHost(String apiHost) {
    this.apiHost = apiHost;
  }

  public void deleteProduct(UUID id) {
    restTemplate.delete(buildUri(id));
  }

  //TODO customer methods
}
