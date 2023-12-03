package com.izanis.productservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Pointcut("execution(* com.izanis.productservice.service.ProductService.*(..))")
  public void serviceMethods() {}

  @AfterReturning(pointcut = "serviceMethods()", returning = "result")
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    String methodName = joinPoint.getSignature().getName();
    log.info("Method {} executed successfully with result: {}", methodName, result);
  }

  @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
    String methodName = joinPoint.getSignature().getName();
    log.error("Method {} failed with exception: {}", methodName, exception.getMessage());
  }
}
