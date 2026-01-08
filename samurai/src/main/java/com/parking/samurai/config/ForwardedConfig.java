package com.parking.samurai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * This filter is used by Spring Boot to properly handle forwarded headers
 * (such as X-Forwarded-For, X-Forwarded-Proto, X-Forwarded-Host, etc.) when the
 * application is running behind a reverse proxy or load balancer (e.g., Render,
 * Cloudflare, Nginx, or any other gateway).
 */

@Configuration
public class ForwardedConfig {

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
