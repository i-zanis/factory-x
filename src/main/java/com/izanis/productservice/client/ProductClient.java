package com.izanis.productservice.client;

import com.izanis.productservice.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Component
@ConfigurationProperties(value = "izanis.com", ignoreUnknownFields = false)
@Slf4j
public class ProductClient {
  public final String PRODUCT_PATH_V1 = "api/v1/product";
  private final RestTemplate restTemplate;
  private String apiHost;

  public ProductClient(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public ProductDto getProductById(UUID id) {
    String uri = UriComponentsBuilder.fromHttpUrl(apiHost)
      .pathSegment(PRODUCT_PATH_V1)
      .pathSegment(id.toString())
      .toUriString();
    return restTemplate.getForObject(uri, ProductDto.class);
  }

  public void setApiHost(String apiHost) {
    this.apiHost = apiHost;
  }
}
