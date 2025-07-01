package com.izanis.productinventoryfailover.web;

import com.izanis.productinventoryfailover.model.ProductInventoryDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

// Failover Service Test
@Component
public class InventoryHandler {
  public Mono<ServerResponse> handleInventory(ServerRequest request) {
    return ServerResponse.ok()
        .body(Mono.just(List.of(new ArrayList<ProductInventoryDto>())), List.class);
  }
}
