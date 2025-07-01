package com.izanis.productservice.service.inventory;

import com.izanis.productservice.service.inventory.model.ProductInventoryDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// TODO(i-zanis): add the proper URL in url =
@FeignClient(name = "inventory-service", url= "http://localhost:8082")
public interface InventoryServiceFeignClient {

  @RequestMapping(method = RequestMethod.GET, value = ProductInventoryServiceFeign.INVENTORY_PATH)
  Optional<ProductInventoryDto> getProductInventory(@RequestBody List<UUID> productId);
}
