package com.factoryx.order.order;

import com.factoryx.order.application.OrderDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc sut;
    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderQueryService orderQueryService;

    @Mock
    private OrderDtoMapper orderDtoMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        sut = MockMvcBuilders.standaloneSetup(new OrderController(orderService, orderQueryService, orderDtoMapper)).build();
    }

    @Test
    void placeOrder_Returns201AndDto() throws Exception {
        UUID customerId = UUID.randomUUID();
        OrderController.PlaceOrderRequest request = OrderController.PlaceOrderRequest.builder()
                .customerId(customerId)
                .items(List.of(new OrderLineItemRequest(UUID.randomUUID(), "SKU-1234", 1)))
                .build();
        Order mockOrder = OrderMother.placed().build();
        OrderDto mockDto = new OrderDto(mockOrder.getId().value(), mockOrder.getCustomerId().value(), java.math.BigDecimal.valueOf(100.0), "USD", List.of());
        given(orderService.placeOrder(any(CustomerId.class), any(List.class))).willReturn(mockOrder);
        given(orderDtoMapper.toDto(mockOrder)).willReturn(mockDto);

        ResultActions actual = sut.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        actual.andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(100.0));
    }
}
