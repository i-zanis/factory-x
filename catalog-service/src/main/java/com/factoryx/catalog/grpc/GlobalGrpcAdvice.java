package com.factoryx.catalog.grpc;

import com.factoryx.common.domain.DomainRuleViolation;
import io.grpc.Status;
import io.grpc.StatusException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GlobalGrpcAdvice {

    @GrpcExceptionHandler(DomainRuleViolation.class)
    public StatusException handleDomainRuleViolation(DomainRuleViolation e) {
        return Status.INVALID_ARGUMENT
                .withDescription("Invalid SKU format: " + e.getMessage())
                .withCause(e)
                .asException();
    }
}
