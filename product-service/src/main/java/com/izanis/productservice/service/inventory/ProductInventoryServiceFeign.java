package com.izanis.productservice.service.inventory;

import static java.util.stream.Collectors.toMap;

import com.izanis.productservice.InventoryServiceProperties;
import com.izanis.productservice.service.inventory.model.ProductInventoryItem;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
// @RequiredArgsConstructor
@Slf4j
@Profile("local-discovery")
public class ProductInventoryServiceFeign {
  public static final String INVENTORY_PATH = "/api/v1/product/inventory";

  private final InventoryServiceFeignClient client;

  private final InventoryServiceProperties properties;

  public ProductInventoryServiceFeign(
      @Qualifier("com.izanis.productservice.service.inventory.InventoryServiceFeignClient")
          InventoryServiceFeignClient client,
      InventoryServiceProperties properties) {
    this.client = client;
    this.properties = properties;
  }

  //  public ProductInventoryService(
  //    RestTemplateBuilder restTemplateBuilder, InventoryServiceProperties properties) {
  //    this.restTemplate =
  //      restTemplateBuilder
  //        .basicAuthentication(properties.getInventoryUser(), properties.getInventoryPassword())
  //        .setConnectTimeout(Duration.ofSeconds(5))
  //        .setReadTimeout(Duration.ofSeconds(20))
  //        .build();
  //    this.properties = properties;
  //  }

  public Map<UUID, Integer> fetchAvailableInventoryForAll(List<UUID> ids) {
    // TODO(i-zanis): check if not needed when AOP Aspect works
    log.debug("Calling ProductInventoryService for IDs: {}", ids);
    return client
        .getProductInventory(ids)
        .map(
            dto ->
                dto.items().stream()
                    .collect(toMap(ProductInventoryItem::id, ProductInventoryItem::inventory)))
        .orElse(Collections.emptyMap());
  }

  // TODO(i-zanis) find if this can be done with a single DB call
  public Integer findAvailableInventory(UUID id) {
    return null;
  }
}
