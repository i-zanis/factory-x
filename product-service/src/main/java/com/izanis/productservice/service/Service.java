package com.izanis.productservice.service;

import java.util.List;
import java.util.UUID;

public interface Service {
  <R> R findById(UUID uuid);

  <R> List<R> findAll();

  <R> R save(R r);

  <R> void update(R r);

}
