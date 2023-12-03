package com.izanis.productservice;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<String> handleConstraintViolationException(ConstraintViolationException e) {
    return e.getConstraintViolations().stream()
        .map(
            constraintViolation ->
                String.format(
                    "%s value '%s' %s",
                    constraintViolation.getPropertyPath(),
                    constraintViolation.getInvalidValue(),
                    constraintViolation.getMessage()))
        .toList();
  }
}
