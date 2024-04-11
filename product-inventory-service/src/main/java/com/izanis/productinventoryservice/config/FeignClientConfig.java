package com.izanis.productinventoryservice.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
      @Value("${izanis.factory.inventory-user}") String user,
      @Value("${izanis.factory.inventory-password}") String password) {
    return new BasicAuthRequestInterceptor(user, password);
  }
}
