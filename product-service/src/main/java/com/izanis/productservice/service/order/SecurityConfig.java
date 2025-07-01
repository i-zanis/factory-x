package com.izanis.productservice.service.order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var user1 = User.withUsername("user1")
                .password(passwordEncoder().encode("pass1"))
                .authorities("WRITE")
                .build();
        var user2 = User.withUsername("user2")
                .password(passwordEncoder().encode("pass2"))
                .roles("USER")
                .build();
        var user3 = User.withUsername("user3")
                .password(passwordEncoder().encode("pass3"))
                .authorities("ROLE_ADMIN", "WRITE")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}