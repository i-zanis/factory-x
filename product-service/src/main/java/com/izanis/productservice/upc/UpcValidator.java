package com.izanis.productservice.upc;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpcValidator implements ConstraintValidator<ValidUpc, String> {

  @Override
  public boolean isValid(String upc, ConstraintValidatorContext context) {
    if (upc == null || upc.length() != 12) {
      return false;
    }
    try {
      int sum = 0;
      for (int i = 0; i < 11; i++) {
        int digit = Character.getNumericValue(upc.charAt(i));
        // even-indexed (0-based) digit
        if (i % 2 == 0) {
          sum += digit;
          // odd-indexed digit
        } else {
          sum += digit * 3;
        }
      }
      int mod = sum % 10;
      int checksum = (mod == 0) ? 0 : 10 - mod;
      return checksum == Character.getNumericValue(upc.charAt(11));
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
