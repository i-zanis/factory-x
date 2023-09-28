package com.izanis.productservice;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CustomerDto(String name, String surname, Integer age,
                          Double balance) {
}

