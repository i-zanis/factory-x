package com.izanis.productservice.service.manufacture;

import com.izanis.model.ProductDto;
import com.izanis.model.events.ManufactureProductEvent;
import com.izanis.model.events.NewInventoryEvent;
import com.izanis.model.events.ProductEvent;
import com.izanis.productservice.config.JmsConfig;
import com.izanis.productservice.entity.Product;
import com.izanis.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManufactureProductListener {
  private final ProductRepository productRepository;
  private final JmsTemplate jmsTemplate;

  @JmsListener(destination = JmsConfig.MANUFACTURE_REQUEST_QUEUE)
  public void listen(ManufactureProductEvent manufactureProductEvent) {
    ProductDto productDto = manufactureProductEvent.getProductDto();
    log.info("Product manufacture request received: {}", productDto);
    Product product = productRepository.getReferenceById(productDto.id());
    ProductDto newProductDto =
        productDto.toBuilder().availableInventory(product.getAvailableInventory()).build();
    ProductEvent newInventoryEvent = NewInventoryEvent.builder().productDto(newProductDto).build();
    log.debug("Sending new inventory event: {}", newInventoryEvent);
    jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
  }
}
