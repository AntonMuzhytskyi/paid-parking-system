package com.parking.samurai.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Swagger / OpenAPI for the Paid Parking Management System backend.
 * Sets API title, version, description, and JWT Bearer authentication globally.
 * This allows testing endpoints with valid JWT tokens directly from Swagger UI.
 */

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Paid Parking Management System", version = "1.0", description = "MVP API"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

public class OpenApiConfig {
}