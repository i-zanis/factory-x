package com.izanis.productinventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ProductInventoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductInventoryServiceApplication.class, args);
  }

}
