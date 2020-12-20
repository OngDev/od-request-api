package com.ongdev.request.configs

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfiguration {
    @Bean
    fun keycloakSpringBootConfigResolver()
            : KeycloakSpringBootConfigResolver = KeycloakSpringBootConfigResolver()
}