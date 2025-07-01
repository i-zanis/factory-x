package com.izanis.productservice.service.inventory.failover;

import com.izanis.productservice.service.inventory.model.ProductInventoryDto;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "inventory-failover", url= "http://localhost:8083", fallback = InventoryServiceFeignClientFailover.class)
public interface InventoryFailoverFeignClient {
  @RequestMapping(method = RequestMethod.GET, value = "/inventory-failover")
  Optional<ProductInventoryDto> getProductInventory();
}
