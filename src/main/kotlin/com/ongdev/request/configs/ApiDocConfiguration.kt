package com.ongdev.request.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("!prod")
@Configuration
class ApiDocConfiguration {
    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI()
                .components(
                        Components()
                                .securitySchemes(
                                        mapOf(
                                                Pair(
                                                        "bearer",
                                                        SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))))
                .info(Info().title("Contact Application API").description(
                        "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3."))
    }
}