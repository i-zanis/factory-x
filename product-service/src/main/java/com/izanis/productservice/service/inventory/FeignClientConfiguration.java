package com.izanis.productservice.service.inventory;

import com.izanis.productservice.InventoryServiceProperties;
import feign.auth.BasicAuthRequestInterceptor;
import feign.Request;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class FeignClientConfiguration {
/// TODO(i-zanis) check if this file is what is required
  /**
   *
   *
   *
   *
   *
   *
   *
   *
   */
    private static final int CONNECT_TIMEOUT_MILLIS = 5000;
    private static final int READ_TIMEOUT_MILLIS = 20000;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(InventoryServiceProperties properties) {
        return new BasicAuthRequestInterceptor(
            properties.getInventoryUser(), 
            properties.getInventoryPassword()
        );
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS, 
            READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS, 
            true
        );
    }
}