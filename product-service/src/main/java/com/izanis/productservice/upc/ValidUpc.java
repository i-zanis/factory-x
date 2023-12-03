package com.izanis.productservice.upc;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpcValidator.class)
public @interface ValidUpc {
    String message() default "Invalid UPC code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
