package com.izanis.productservice.service.inventory;

import static java.util.stream.Collectors.*;

import com.izanis.productservice.InventoryServiceProperties;
import com.izanis.productservice.service.inventory.model.ProductInventoryDto;
import com.izanis.productservice.service.inventory.model.ProductInventoryItem;
import java.time.Duration;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("!local-discovery")
@Slf4j
public class ProductInventoryService {
  public static final String INVENTORY_PATH = "/api/v1/product/inventory";
  private final RestTemplate restTemplate;
  private final InventoryServiceProperties properties;

  public ProductInventoryService(
      RestTemplateBuilder restTemplateBuilder, InventoryServiceProperties properties) {
    this.restTemplate =
        restTemplateBuilder
            .basicAuthentication(properties.getInventoryUser(), properties.getInventoryPassword())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(20))
            .build();
    this.properties = properties;
  }

  public Map<UUID, Integer> findAvailableInventoryForAll(List<UUID> ids) {
    // TODO(i-zanis): check if not needed when AOP Aspect works
    log.debug("Calling ProductInventoryService for IDs: {}", ids);
    Optional<ProductInventoryDto> response = fetchInventoryData(ids);
    return response
        .map(
            dto ->
                dto.items().stream()
                    .collect(toMap(ProductInventoryItem::id, ProductInventoryItem::inventory)))
        .orElse(Collections.emptyMap());
  }

  private Optional<ProductInventoryDto> fetchInventoryData(List<UUID> ids) {
    String url = properties.getProductInventoryServiceHost() + INVENTORY_PATH;
    HttpEntity<List<UUID>> requestEntity = new HttpEntity<>(ids);
    ResponseEntity<ProductInventoryDto> response =
        restTemplate.exchange(
            url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});
    return Optional.of(response).map(ResponseEntity::getBody);
  }

  // TODO(i-zanis) find if this can be done with a single DB call
  public Integer findAvailableInventory(UUID id) {
    return null;
  }
}
