package com.izanis.productorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ProductOrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductOrderServiceApplication.class, args);
  }

}
