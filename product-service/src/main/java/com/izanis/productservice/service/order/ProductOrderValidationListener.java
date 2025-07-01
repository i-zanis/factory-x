package com.izanis.productservice.service.order;

import com.izanis.model.events.ValidateOrderRequest;
import com.izanis.productservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.izanis.model.events.ValidateOrderResult;

@RequiredArgsConstructor
@Component
public class ProductOrderValidationListener {
  private final ProductOrderValidator validator;
  private final JmsTemplate jmsTemplate;

//  @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
  public void listener(ValidateOrderRequest validateOrderRequest) {
    Boolean isValid = validator.validateOrder(validateOrderRequest.getProductOrderDto());

    jmsTemplate.convertAndSend(
        JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
        ValidateOrderResult.builder()
            .valid(isValid)
            .orderId(validateOrderRequest.getProductOrderDto().id())
            .build());
  }
}
