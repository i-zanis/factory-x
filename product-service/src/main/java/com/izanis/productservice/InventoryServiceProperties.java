package com.izanis.productservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "izanis.factory")
//@EnableConfigurationProperties(InventoryServiceProperties.class)
@Getter
@Setter
public class InventoryServiceProperties {
  private String inventoryUser;
  private String inventoryPassword;
  private String productInventoryServiceHost;
}
