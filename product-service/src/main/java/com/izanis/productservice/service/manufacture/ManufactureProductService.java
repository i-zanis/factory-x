package com.izanis.productservice.service.manufacture;

import com.izanis.model.events.ManufactureProductEvent;
import com.izanis.productservice.config.JmsConfig;
import com.izanis.productservice.entity.Product;
import com.izanis.productservice.repository.ProductRepository;
import com.izanis.productservice.service.inventory.ProductInventoryService;
import com.izanis.productservice.web.mapper.ProductMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.izanis.productservice.web.mapper.ProductMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManufactureProductService {
  private final ProductRepository productRepository;
  private final ProductInventoryService productInventoryService;
  private final JmsTemplate jmsTemplate;
  private final ProductMapper productMapper;

//  @Scheduled(fixedRate = 5000)
//  public void checkForLowInventory() {
//    List<Product> products = productRepository.findAll();
//    List<UUID> ids = products.stream().map(Product::getId).toList();
//    Map<UUID, Integer> inventoryMap = productInventoryService.findAvailableInventoryForAll(ids);
//    products.forEach(product -> processProductInventory(product, inventoryMap));
//  }

  private void processProductInventory(Product product, Map<UUID, Integer> inventoryMap) {
    Integer availableInventory = inventoryMap.getOrDefault(product.getId(), 0);
    log.debug("Inventory: {} / {}", product.getName(), product.getId());
    log.debug("Min Available: {}", product.getMinAvailableInventory());
    log.debug("Available: {}", availableInventory);

    if (isInventoryBelowThreshold(product, availableInventory)) {
      sendManufactureRequest(product);
    }
  }

  private boolean isInventoryBelowThreshold(Product product, Integer availableInventory) {
    return availableInventory != null && product.getMinAvailableInventory() >= availableInventory;
  }

  private void sendManufactureRequest(Product product) {
    jmsTemplate.convertAndSend(
        JmsConfig.MANUFACTURE_REQUEST_QUEUE,
        ManufactureProductEvent.builder().productDto(productMapper.toProductDto(product)));
  }
}
