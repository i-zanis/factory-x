package com.izanis.productservice.service.inventory;

import com.izanis.productservice.service.inventory.model.ProductInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "inventory-service")
public class InventoryServiceFeignClient {

  ResponseEntity<ProductInventoryDto> retrieveAvailableInventory(UUID)
}
