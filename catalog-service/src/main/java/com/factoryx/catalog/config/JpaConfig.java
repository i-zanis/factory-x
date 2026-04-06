package com.factoryx.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // Normally, this would extract the current user from the SecurityContext
        // e.g., SecurityContextHolder.getContext().getAuthentication().getName()
        // For now, we return a default system user until Spring Security is added.
        return () -> Optional.of("system");
    }
}
