package com.example.demo.config;


import com.example.demo.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return new SpringAuditAwareImpl();
    }
}

class SpringAuditAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal()))) {
            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getId());
        }
        return Optional.empty();
    }
}

