package com.izanis.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class ProductServiceApplicationTests {

  @Test
  void contextLoads() {
    int[] nums = {1,2,34};
    int x = Collections.max(Arrays.asList(nums));
  }

}
