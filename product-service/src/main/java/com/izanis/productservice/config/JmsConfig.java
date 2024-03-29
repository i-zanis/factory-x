package com.izanis.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {
  public static final String MANUFACTURE_REQUEST_QUEUE = "manufacture-request";
  public static final String NEW_INVENTORY_QUEUE = "new-inventory";
  public static final String VALIDATE_ORDER_QUEUE = "validate-order";
  public static final String VALIDATE_ORDER_RESPONSE_QUEUE = "validate-order-response";

  @Bean
  public MappingJackson2MessageConverter getConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }
}
